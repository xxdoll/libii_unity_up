package com.libii.sso.unity.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.libii.sso.common.core.AbstractService;
import com.libii.sso.common.exception.CustomException;
import com.libii.sso.common.restResult.PageParam;
import com.libii.sso.common.restResult.ResultCode;
import com.libii.sso.common.zip.FileUtils;
import com.libii.sso.s3.service.ObsOperationService;
import com.libii.sso.unity.dao.HotUpdateMapper;
import com.libii.sso.unity.domain.HotUpdateResourceInfo;
import com.libii.sso.unity.dto.*;
import com.libii.sso.unity.service.HotUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: fengchenxin
 * @ClassName: HotUpdateServiceImpl
 * @date: 2023-04-23  15:27
 * @Description: TODO
 * @version: 1.0
 */
@Slf4j
@Service
@Transactional
public class HotUpdateServiceImpl extends AbstractService<HotUpdateResourceInfo> implements HotUpdateService {

    @Resource
    private HotUpdateMapper hotUpdateMapper;

    @Resource
    private Cache<String, Integer> whiteListCache;

    @Resource
    private Cache<String, List<HotUpdateResourceInfo>> hotUpdateResourceCache;

    @Resource
    private ObsOperationService obsOperationService;

    @Value("${local-server.server-type}")
    private String serverType;

    @Override
    public void uploadResource(HotUpdateResourceInputDTO inputDTO) {
        MultipartFile resource = inputDTO.getResource();
        if(null!=resource&&!resource.isEmpty()){
            String fileName = resource.getOriginalFilename();
            // 判断文件是否为zip文件
            if (!fileName.endsWith("zip")) {
                throw new RuntimeException("传入文件格式错误" + fileName);
            }
            Example example = new Example(HotUpdateResourceInfo.class);
            example.and()
                    .andEqualTo("resourceVersion",inputDTO.getResourceVersion())
                    .andEqualTo("gameId",inputDTO.getGameId())
                    .andEqualTo("platform", inputDTO.getPlatform())
                    .andEqualTo("channel",inputDTO.getChannel())
                    .andEqualTo("customId",inputDTO.getCustomId())
                    .andEqualTo("unityVersion",inputDTO.getUnityVersion())
                    .andEqualTo("appVersionNum",inputDTO.getAppVersionNum());
            int count = hotUpdateMapper.selectCountByExample(example);
            if(0!=count){
                throw new CustomException(ResultCode.RESOURCE_EXIST);
            }
            // cdn路径构建  gameId/platform/channel/customId/resourceVersion
            String gameId = inputDTO.getGameId();
            String platform = inputDTO.getPlatform();
            String channel = inputDTO.getChannel();
            String customId = inputDTO.getCustomId();
            String resourceVersion = inputDTO.getResourceVersion();
            String basePath = gameId + "/" + platform + "/" + channel + "/" + customId + "/";
            String serverUrl;
            // 本地拷贝File
            File tempFile = new File(basePath + resourceVersion + ".zip");
            try {
                FileUtils.saveFile(resource,tempFile);
                // 华为解压上传
                obsOperationService.initObsClient();
                serverUrl = obsOperationService.getServer();
                obsOperationService.unZipToCDN(tempFile,basePath);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException(ResultCode.CDN_RESOURCE_UPLOAD_ERROR);
            } finally {
                boolean delete = tempFile.delete();
                log.info("本地压缩包已删除： " + delete);
            }

            HotUpdateResourceInfo hotUpdateResourceInfo = new HotUpdateResourceInfo();
            int status = serverType.equals("inet")?1:0;
            String appVersionStr = inputDTO.getAppVersionStr();
            Integer appVersionNum = inputDTO.getAppVersionNum();
            BeanUtils.copyProperties(inputDTO,hotUpdateResourceInfo);
            hotUpdateResourceInfo.setArea("all");
            hotUpdateResourceInfo.setIsForceUpdate(false);
            hotUpdateResourceInfo.setMaxAppVersionNum(appVersionNum);
            hotUpdateResourceInfo.setMinAppVersionNum(appVersionNum);
            hotUpdateResourceInfo.setMaxAppVersionStr(appVersionStr);
            hotUpdateResourceInfo.setMinAppVersionStr(appVersionStr);
            hotUpdateResourceInfo.setServerUrl(serverUrl);
            hotUpdateResourceInfo.setStatus(status);
            hotUpdateResourceInfo.setCreateTime(new Date());
            hotUpdateMapper.insert(hotUpdateResourceInfo);
        }

    }

    @Override
    public Map<String, Object> queryResource(HotUpdateResourceQueryDTO queryDTO) {
        String gameId = queryDTO.getGameId();
        String platform = queryDTO.getPlatform();
        String channel = queryDTO.getChannel();
        String customId = queryDTO.getCustomId();
        String unityVersion = queryDTO.getUnityVersion();
        String resourceVersion = queryDTO.getResourceVersion();
        Integer appVersionNum = queryDTO.getAppVersionNum();
        String deviceId = queryDTO.getDeviceId();

        Map<String, Object> resultMap = new HashMap<>();
        String hotUpdateKey = gameId + "_" + platform + "_" + channel + "_" + customId + "_" + unityVersion;
        //初始collect
        List<HotUpdateResourceInfo> cacheCollect = hotUpdateResourceCache.getIfPresent(hotUpdateKey);
        if(!CollectionUtils.isEmpty(cacheCollect)){
            Boolean globalIsForceUpdate = false;
            Optional<HotUpdateResourceInfo> first = cacheCollect.stream().filter(c -> c.getAppVersionNum() > appVersionNum).findFirst();
            if(first.isPresent()){
                HotUpdateResourceInfo hotUpdateResourceInfo = first.get();
                globalIsForceUpdate = hotUpdateResourceInfo.getIsForceUpdate();
                resultMap.put("isForceUpdate",globalIsForceUpdate);
            }

            List<HotUpdateResourceInfo> appVersionFilterCollect = cacheCollect.stream().filter(s -> appVersionNum >= s.getMinAppVersionNum() && appVersionNum <= s.getMaxAppVersionNum()).collect(Collectors.toList());
            //初始条件下集合
            if(!CollectionUtils.isEmpty(appVersionFilterCollect)){
                if(StringUtils.hasText(deviceId)){
                    //匹配status=0 审核 集合
                    List<HotUpdateResourceInfo> statusFilterCollect = appVersionFilterCollect.stream().filter(a -> 0 == a.getStatus()).collect(Collectors.toList());
                    if(!CollectionUtils.isEmpty(statusFilterCollect)){
                        Integer ifPresent = whiteListCache.getIfPresent(gameId + "-" + deviceId);
                        if(null!=ifPresent){
                            Optional<HotUpdateResourceInfo> max = statusFilterCollect.stream().max(Comparator.comparing(HotUpdateResourceInfo::getResourceVersion));
                            if(max.isPresent()){
                                HotUpdateResourceInfo target = max.get();
                                resultMap.put("resourcesVersion",target.getResourceVersion());
                                resultMap.put("serverUrl",target.getServerUrl());
                                return resultMap;
                            }
                        }else {
                            Optional<HotUpdateResourceInfo> resourceEqualsResult = statusFilterCollect.stream().filter(s -> s.getResourceVersion().equals(resourceVersion)).findFirst();
                            if(resourceEqualsResult.isPresent()){
                                HotUpdateResourceInfo target = resourceEqualsResult.get();
                                resultMap.put("resourcesVersion",target.getResourceVersion());
                                resultMap.put("serverUrl",target.getServerUrl());
                            }
                        }
                    }
                }
                //匹配status=1集合
                List<HotUpdateResourceInfo> status1FilterCollect = appVersionFilterCollect.stream().filter(a -> 1 == a.getStatus()).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(status1FilterCollect)){
                    Optional<HotUpdateResourceInfo> max = status1FilterCollect.stream().max(Comparator.comparing(HotUpdateResourceInfo::getResourceVersion));
                    if(max.isPresent()) {
                        HotUpdateResourceInfo target = max.get();
                        resultMap.put("resourcesVersion", target.getResourceVersion());
                        resultMap.put("serverUrl", target.getServerUrl());
                        return resultMap;
                    }
                }
            }
        }
        resultMap.put("resourcesVersion",null);
        resultMap.put("serverUrl",null);
        resultMap.put("isForceUpdate",false);
        return resultMap;
    }

    @Override
    public List<HotUpdateResourceInfo> list(PageParam pageParam, HotUpdateResourceConditionDTO conditionDTO) {
        Example example = new Example(HotUpdateResourceInfo.class);
        if (null != conditionDTO) {
            if (StringUtils.hasText(conditionDTO.getGameId())) {
                example.and().andEqualTo("gameId",conditionDTO.getGameId());
            }
            if (StringUtils.hasText(conditionDTO.getChannel())) {
                example.and().andEqualTo("channel",conditionDTO.getChannel());
            }
            if (StringUtils.hasText(conditionDTO.getPlatform())) {
                example.and().andEqualTo("platform", conditionDTO.getPlatform());
            }
            if (StringUtils.hasText(conditionDTO.getCustomId())) {
                example.and().andEqualTo("customId", conditionDTO.getCustomId());
            }
        }
        example.setOrderByClause("create_time desc");
        PageHelper.startPage(pageParam.getPage(), pageParam.getSize());
        return hotUpdateMapper.selectByExample(example);
    }

    @Override
    public void cut(HotUpdateResourceCutDTO cutDTO) {
        HotUpdateResourceInfo oldHotUpdateResourceInfo = hotUpdateMapper.selectByPrimaryKey(cutDTO.getId());
        Date date = new Date();
        //强更更改，相同条件下不同版本
        if(!cutDTO.getIsForceUpdate().equals(oldHotUpdateResourceInfo.getIsForceUpdate())){
            Example example = new Example(HotUpdateResourceInfo.class);
            example.and()
                    .andEqualTo("gameId",oldHotUpdateResourceInfo.getGameId())
                    .andEqualTo("channel",oldHotUpdateResourceInfo.getChannel())
                    .andEqualTo("platform", oldHotUpdateResourceInfo.getPlatform())
                    .andEqualTo("customId", oldHotUpdateResourceInfo.getCustomId())
                    .andEqualTo("unityVersion",oldHotUpdateResourceInfo.getUnityVersion())
                    .andEqualTo("appVersionStr",oldHotUpdateResourceInfo.getAppVersionStr());
            List<HotUpdateResourceInfo> hotUpdateResourceInfos = hotUpdateMapper.selectByExample(example);
            if(!CollectionUtils.isEmpty(hotUpdateResourceInfos) && hotUpdateResourceInfos.size()>1){
                hotUpdateResourceInfos.stream().forEach(h->{
                    h.setIsForceUpdate(cutDTO.getIsForceUpdate());
                    h.setUpdateTime(date);
                });
                hotUpdateMapper.updateBatchByPrimaryKeySelective(hotUpdateResourceInfos);
            }
        }
        oldHotUpdateResourceInfo.setStatus(cutDTO.getStatus());
        oldHotUpdateResourceInfo.setIsForceUpdate(cutDTO.getIsForceUpdate());
        oldHotUpdateResourceInfo.setMinAppVersionStr(cutDTO.getMinAppVersionStr());
        oldHotUpdateResourceInfo.setMaxAppVersionStr(cutDTO.getMaxAppVersionStr());
        oldHotUpdateResourceInfo.setMinAppVersionNum(cutDTO.getMinAppVersionNum());
        oldHotUpdateResourceInfo.setMaxAppVersionNum(cutDTO.getMaxAppVersionNum());
        oldHotUpdateResourceInfo.setUpdateTime(date);
        hotUpdateMapper.updateByPrimaryKeySelective(oldHotUpdateResourceInfo);
    }

    @Override
    public void deleteResource(Integer id) {
        hotUpdateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> queryTree(){
        List<HotUpdateResourceInfo> hotUpdateResourceInfos = hotUpdateMapper.selectAll();
        if(!CollectionUtils.isEmpty(hotUpdateResourceInfos)){
            Map<String, Map<String, Map<String, Map<String, Map<String,String>>>>> collect =
                    hotUpdateResourceInfos
                            .stream()
                            .collect(
                                    Collectors.groupingBy(HotUpdateResourceInfo::getGameId,
                                            Collectors.groupingBy(HotUpdateResourceInfo::getPlatform,
                                                    Collectors.groupingBy(HotUpdateResourceInfo::getChannel,
                                                            Collectors.groupingBy(HotUpdateResourceInfo::getCustomId,
                                                                    Collectors.toMap(HotUpdateResourceInfo::getUnityVersion,HotUpdateResourceInfo::getAppVersionStr,(v1,v2)->v2)))))
                            );
            return collect;
        }
        return null;
    }

    @Override
    public List<Map<String,Integer>> queryVersion(HotUpdateResourceConditionDTO conditionDTO) {
        Example example = new Example(HotUpdateResourceInfo.class);
        example.and()
                .andEqualTo("gameId",conditionDTO.getGameId())
                .andEqualTo("channel",conditionDTO.getChannel())
                .andEqualTo("platform", conditionDTO.getPlatform())
                .andEqualTo("customId", conditionDTO.getCustomId())
                .andEqualTo("unityVersion",conditionDTO.getUnityVersion());
        List<HotUpdateResourceInfo> hotUpdateResourceInfos = hotUpdateMapper.selectByExample(example);
        List<Map<String,Integer>> collect = hotUpdateResourceInfos.stream().map(h->{
            Map<String, Integer> filterMap = new HashMap<>();
            filterMap.put(h.getAppVersionStr(),h.getAppVersionNum());
            return filterMap;
        }).distinct().collect(Collectors.toList());
        return collect;
    }

    @Scheduled(fixedRateString = "${timed-task.hot-update-refresh-cycle}")
    public void hotUpdateResourceCacheFilling(){
        List<HotUpdateResourceInfo> hotUpdateResourceInfos = hotUpdateMapper.selectAll();
        Map<String, List<HotUpdateResourceInfo>> collect = hotUpdateResourceInfos.stream()
                .filter(s->-1!=s.getStatus())
                .collect(Collectors.groupingBy(s ->
                                s.getGameId() + "_" + s.getPlatform() + "_" + s.getChannel() + "_" + s.getCustomId() + "_" + s.getUnityVersion()
                        , Collectors.toList()));
        hotUpdateResourceCache.invalidateAll();
        hotUpdateResourceCache.putAll(collect);
    }
}
