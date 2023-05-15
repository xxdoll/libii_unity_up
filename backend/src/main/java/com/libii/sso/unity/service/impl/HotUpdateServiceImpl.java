package com.libii.sso.unity.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.libii.sso.common.core.AbstractService;
import com.libii.sso.common.exception.CustomException;
import com.libii.sso.common.restResult.PageParam;
import com.libii.sso.common.restResult.ResultCode;
import com.libii.sso.unity.dao.HotUpdateMapper;
import com.libii.sso.unity.dao.ProjectMapper;
import com.libii.sso.unity.domain.HotUpdateResourceInfo;
import com.libii.sso.unity.domain.Project;
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
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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
    private ProjectMapper projectMapper;

    @Value("${s3-server-url}")
    private String serverUrl;

    @Override
    public Map<String, Object> queryResource(HotUpdateResourceQueryDTO queryDTO) {
        String bundleId = queryDTO.getBundleId();
        String platform = queryDTO.getPlatform();
        String channel = queryDTO.getChannel();
        String customId = queryDTO.getCustomId();
        String unityVersion = queryDTO.getUnityVersion();
        String resourceVersion = queryDTO.getResourceVersion();
        Integer appVersionNum = queryDTO.getAppVersionNum();
        String deviceId = queryDTO.getDeviceId();

        Map<String, Object> resultMap = new HashMap<>();
        String hotUpdateKey = bundleId + "_" + platform + "_" + channel + "_" + customId + "_" + unityVersion;
        //初始collect
        List<HotUpdateResourceInfo> cacheCollect = hotUpdateResourceCache.getIfPresent(hotUpdateKey);
        if(!CollectionUtils.isEmpty(cacheCollect)){
            //初始条件下集合（结果2）
            List<HotUpdateResourceInfo> appVersionFilterCollect = cacheCollect.stream().filter(s -> appVersionNum >= s.getMinAppVersionNum() && appVersionNum <= s.getMaxAppVersionNum()).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(appVersionFilterCollect)){
                //请求设备校验
                if(StringUtils.hasText(deviceId)){
                    //匹配status=审核 集合（结果3）
                    List<HotUpdateResourceInfo> statusFilterCollect = appVersionFilterCollect.stream().filter(a -> 0 == a.getStatus()).collect(Collectors.toList());
                    if(!CollectionUtils.isEmpty(statusFilterCollect)){
                        //白名单校验
                        Integer ifPresent = whiteListCache.getIfPresent(bundleId + "-" + deviceId);
                        // 资源版本最大资源
                        if(null!=ifPresent){
                            Optional<HotUpdateResourceInfo> max = statusFilterCollect.stream().max(Comparator.comparing(HotUpdateResourceInfo::getResourceVersion));
                            if(max.isPresent()){
                                HotUpdateResourceInfo target = max.get();
                                resultMap.put("resourcesVersion",target.getResourceVersion());
                                resultMap.put("serverUrl",target.getServerUrl());
                                resultMap.put("timeout", target.getTimeout());
                                return resultMap;
                            }
                        }else {
                            Optional<HotUpdateResourceInfo> resourceEqualsResult = statusFilterCollect.stream().filter(s -> s.getResourceVersion().equals(resourceVersion)).findFirst();
                            if(resourceEqualsResult.isPresent()){
                                HotUpdateResourceInfo target = resourceEqualsResult.get();
                                resultMap.put("resourcesVersion",target.getResourceVersion());
                                resultMap.put("serverUrl",target.getServerUrl());
                                resultMap.put("timeout", target.getTimeout());
                                return resultMap;
                            }
                        }
                    }
                }
                //匹配status=1集合（结果4）
                List<HotUpdateResourceInfo> status1FilterCollect = appVersionFilterCollect.stream().filter(a -> 1 == a.getStatus()).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(status1FilterCollect)){
                    Optional<HotUpdateResourceInfo> max = status1FilterCollect.stream().max(Comparator.comparing(HotUpdateResourceInfo::getResourceVersion));
                    if(max.isPresent()) {
                        HotUpdateResourceInfo target = max.get();
                        resultMap.put("resourcesVersion", target.getResourceVersion());
                        resultMap.put("serverUrl", target.getServerUrl());
                        resultMap.put("timeout", target.getTimeout());
                        return resultMap;
                    }
                }
            }
        }
        resultMap.put("resourcesVersion",null);
        resultMap.put("serverUrl",null);
        resultMap.put("timeout",0);
        return resultMap;
    }


    @Override
    public List<HotUpdateResourceInfo> list(PageParam pageParam, HotUpdateResourceConditionDTO conditionDTO) {
        Example example = new Example(HotUpdateResourceInfo.class);
        if (null != conditionDTO) {
            if (StringUtils.hasText(conditionDTO.getBundleId())) {
                example.and().andEqualTo("bundleId",conditionDTO.getBundleId());
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
            if (StringUtils.hasText(conditionDTO.getAppVersionStr())) {
                example.and().andEqualTo("appVersionStr", conditionDTO.getAppVersionStr());
            }
        }
        example.setOrderByClause("create_time desc");
        PageHelper.startPage(pageParam.getPage(), pageParam.getSize());
        return hotUpdateMapper.selectByExample(example);
    }

    @Override
    public void cut(HotUpdateResourceCutDTO cutDTO) {
        // 发布资源修改检测

//        String newMinAppVersionStr = cutDTO.getMinAppVersionStr();
//        String newMaxAppVersionStr = cutDTO.getMaxAppVersionStr();
//        Boolean isForceUpdate = cutDTO.getIsForceUpdate();
//        String area = cutDTO.getArea();
//        Integer timeout = cutDTO.getTimeout();
//        Integer status = cutDTO.getStatus();
//        if(!newMinAppVersionStr.equals(oldHotUpdateResourceInfo.getMinAppVersionStr())||
//                !newMaxAppVersionStr.equals(oldHotUpdateResourceInfo.getMaxAppVersionStr())||
//                        !isForceUpdate.equals(oldHotUpdateResourceInfo.getIsForceUpdate())||
//                                !area.equals(oldHotUpdateResourceInfo.getArea())||
//                                        !timeout.equals(oldHotUpdateResourceInfo.getTimeout())){
//            if(status.equals(1)){
//                throw new CustomException(ResultCode.ACTIVE_RESOURCE_CAN_NOT_CHANGE);
//            }
//        }
        HotUpdateResourceInfo oldHotUpdateResourceInfo = hotUpdateMapper.selectByPrimaryKey(cutDTO.getId());
        if(oldHotUpdateResourceInfo.getStatus().equals(1)){
            String newMinAppVersionStr = cutDTO.getMinAppVersionStr();
            String newMaxAppVersionStr = cutDTO.getMaxAppVersionStr();
            String area = cutDTO.getArea();
            Integer timeout = cutDTO.getTimeout();
            Integer status = cutDTO.getStatus();
            if(!newMinAppVersionStr.equals(oldHotUpdateResourceInfo.getMinAppVersionStr())||
                    !newMaxAppVersionStr.equals(oldHotUpdateResourceInfo.getMaxAppVersionStr())||
                                !area.equals(oldHotUpdateResourceInfo.getArea())||
                                        !timeout.equals(oldHotUpdateResourceInfo.getTimeout())){
                if(status.equals(1)){
                    throw new CustomException(ResultCode.ACTIVE_RESOURCE_CAN_NOT_CHANGE);
                }
            }
        }
        Date date = new Date();
        oldHotUpdateResourceInfo.setStatus(cutDTO.getStatus());
        oldHotUpdateResourceInfo.setTimeout(cutDTO.getTimeout());
        oldHotUpdateResourceInfo.setServerUrl(cutDTO.getServerUrl());
        oldHotUpdateResourceInfo.setMinAppVersionStr(cutDTO.getMinAppVersionStr());
        oldHotUpdateResourceInfo.setMaxAppVersionStr(cutDTO.getMaxAppVersionStr());
        oldHotUpdateResourceInfo.setMinAppVersionNum(cutDTO.getMinAppVersionNum());
        oldHotUpdateResourceInfo.setMaxAppVersionNum(cutDTO.getMaxAppVersionNum());
        oldHotUpdateResourceInfo.setUpdateTime(date);
        hotUpdateMapper.updateByPrimaryKeySelective(oldHotUpdateResourceInfo);
    }


    @Override
    public Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> queryTree(){
        List<HotUpdateResourceInfo> hotUpdateResourceInfos = hotUpdateMapper.selectAll();
        if(!CollectionUtils.isEmpty(hotUpdateResourceInfos)){
            Map<String, Map<String, Map<String, Map<String, Map<String,String>>>>> collect =
                    hotUpdateResourceInfos
                            .stream()
                            .collect(
                                    Collectors.groupingBy(HotUpdateResourceInfo::getBundleId,
                                            Collectors.groupingBy(HotUpdateResourceInfo::getPlatform,
                                                    Collectors.groupingBy(HotUpdateResourceInfo::getChannel,
                                                            Collectors.groupingBy(HotUpdateResourceInfo::getCustomId,
                                                                    Collectors.toMap(HotUpdateResourceInfo::getAppVersionStr,HotUpdateResourceInfo::getUnityVersion,(v1,v2)->v2)))))
                            );
            return collect;
        }
        return null;
    }

    @Override
    public List<Map<String,Integer>> queryVersion(HotUpdateResourceConditionDTO conditionDTO) {
        Example example = new Example(HotUpdateResourceInfo.class);
        example.and()
                .andEqualTo("bundleId",conditionDTO.getBundleId())
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

    @Override
    public void consumeTestConfig(HotUpdateInetInputDTO inputDTO) {
        // project表新增修改检测
        String bundleId = inputDTO.getBundleId();
        String name = inputDTO.getName();
        Example example = new Example(Project.class);
        example.and().andEqualTo("code",bundleId);
        List<Project> projects = projectMapper.selectByExample(example);
        Date date = new Date();
        if(!CollectionUtils.isEmpty(projects)){
            Project project = projects.get(0);
            String oldName = project.getName();
            if(oldName.equals(name)){
                project.setName(name);
                project.setUpdateTime(date);
                projectMapper.updateByPrimaryKeySelective(project);
            }
        }else {
            Project project = new Project();
            project.setName(name);
            project.setCode(bundleId);
            project.setCreateTime(date);
            projectMapper.insert(project);
        }

        HotUpdateResourceInfo hotUpdateResourceInfo = new HotUpdateResourceInfo();
        BeanUtils.copyProperties(inputDTO,hotUpdateResourceInfo);
        hotUpdateResourceInfo.setCreateTime(date);
        hotUpdateResourceInfo.setServerUrl(serverUrl);
        hotUpdateResourceInfo.setStatus(0);//外网默认设置审核
        try {
            hotUpdateMapper.insert(hotUpdateResourceInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean publishCheck(Long id){
        HotUpdateResourceInfo oldResource = hotUpdateMapper.selectByPrimaryKey(id);
        Example example = new Example(HotUpdateResourceInfo.class);
        example.and()
                .andEqualTo("bundleId",oldResource.getBundleId())
                .andEqualTo("channel",oldResource.getChannel())
                .andEqualTo("platform", oldResource.getPlatform())
                .andEqualTo("customId", oldResource.getCustomId())
                .andEqualTo("unityVersion",oldResource.getUnityVersion())
                .andEqualTo("appVersionStr",oldResource.getAppVersionStr())
                .andEqualTo("status",1);
        List<HotUpdateResourceInfo> hotUpdateResourceInfos = hotUpdateMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(hotUpdateResourceInfos)&&hotUpdateResourceInfos.size()<=1){
            return false;
        }
        return true;
    }

    @Scheduled(fixedRateString = "${timed-task.hot-update-refresh-cycle}")
    public void hotUpdateResourceCacheFilling(){
        Example example = new Example(HotUpdateResourceInfo.class);
        example.and().andNotEqualTo("status",-1);
        List<HotUpdateResourceInfo> hotUpdateResourceInfos = hotUpdateMapper.selectByExample(example);
        Map<String, List<HotUpdateResourceInfo>> collect = hotUpdateResourceInfos.stream()
                .filter(s->-1!=s.getStatus())
                .collect(Collectors.groupingBy(s ->
                                s.getBundleId() + "_" + s.getPlatform() + "_" + s.getChannel() + "_" + s.getCustomId() + "_" + s.getUnityVersion()
                        , Collectors.toList()));
        hotUpdateResourceCache.invalidateAll();
        hotUpdateResourceCache.putAll(collect);
    }
}
