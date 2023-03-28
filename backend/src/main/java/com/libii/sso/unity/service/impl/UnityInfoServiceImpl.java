package com.libii.sso.unity.service.impl;

import com.github.pagehelper.PageHelper;
import com.libii.sso.common.core.AbstractService;
import com.libii.sso.common.exception.CustomException;
import com.libii.sso.common.model.Constant;
import com.libii.sso.common.restResult.PageParam;
import com.libii.sso.common.restResult.ResultCode;
import com.libii.sso.common.util.LocalCdnUtil;
import com.libii.sso.common.zip.FileUtil;
import com.libii.sso.common.zip.FileUtils;
import com.libii.sso.common.zip.ZipUtil;
import com.libii.sso.unity.dao.UnityInfoMapper;
import com.libii.sso.unity.domain.UnityInfo;
import com.libii.sso.unity.dto.ConfigInputDTO;
import com.libii.sso.unity.dto.QueryUnityDTO;
import com.libii.sso.unity.dto.UnityInputDTO;
import com.libii.sso.unity.service.UnityInfoService;
import com.obs.services.ObsClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Generate
 * @Description: // TODO 为类添加注释
 * @date 2021-03-01 03:18:38
 */
@Slf4j
@Service
@Transactional
public class UnityInfoServiceImpl extends AbstractService<UnityInfo> implements UnityInfoService {
    @Resource
    private UnityInfoMapper unityInfoMapper;

    @Value("${cdn.server}")
    private String cdn_server;
    @Value("${test.url}")
    private String test_url;
    @Value("${test.path}")
    private String test_path;
    @Autowired
    private LocalCdnUtil cdnUtil;

    /**
     * 根据分页、排序信息和检索条件查询 @size 条 字典表数据
     *
     * @param pageParam 分页参数
     * @param queryDTO  查询关键字
     * @return
     */
    @Override
    public List<UnityInfo> list(PageParam pageParam, QueryUnityDTO queryDTO) {
        Example example = new Example(UnityInfo.class);
        if (null != queryDTO) {
            if (StringUtils.isNotEmpty(queryDTO.getCode())) {
                example.and().andLike("code", "%" + queryDTO.getCode() + "%");
            }
            if (StringUtils.isNotEmpty(queryDTO.getVersion())) {
                example.and().andEqualTo("version", queryDTO.getVersion());
            }
            if (null != queryDTO.getStatus()) {
                example.and().andEqualTo("status", queryDTO.getStatus());
            }
        }
        example.setOrderByClause("create_time desc");
        PageHelper.startPage(pageParam.getPage(), pageParam.getSize());
        return unityInfoMapper.selectByExample(example);
    }

    /**
     * 上传
     *
     * @param inputDTO
     */
    @Override
    public void uploadArchive(UnityInputDTO inputDTO) {
        MultipartFile file = inputDTO.getZipFile();
        String code = inputDTO.getCode();
        String name = inputDTO.getName();
        Integer status = inputDTO.getStatus();
        if (null != file && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            // 判断文件是否为zip文件
            if (!fileName.endsWith("zip")) {
                throw new RuntimeException("传入文件格式错误" + fileName);
            }
            String version = fileName.substring(0, fileName.lastIndexOf(".zip"));
            // 判断服务器是否有此版本的资源
            Example example = new Example(UnityInfo.class);
            example.and().andEqualTo("code", code)
                    .andEqualTo("version", version);
            int count = unityInfoMapper.selectCountByExample(example);
            if (count != 0) {
                throw new CustomException(ResultCode.VERSION_IS_EXIST);
            }
            // 保存文件到服务器并解压
            Date date = new Date();
//            String version = DateUtils.DateToString(date, "yyyyMMddHHmmss");
            String basePath = code + "/" + version + "/";
            File tempFile = new File(test_path + basePath + "temp.zip");
            UnityInfo info = new UnityInfo();
            try {
                // 文件写到磁盘 NIO的方式
                FileUtils.saveFile(file, tempFile);
                // 普通的方式
//                 file.transferTo(tempFile);
                // 上传到CDN
                if (status == Constant.STATUS_PROD) {
                    ObsClient obsClient = cdnUtil.getObsClient();
                    String bucket = cdnUtil.getBucket();
                    ZipUtil.unZipToCDN(tempFile, obsClient, bucket, basePath);
                    info.setCdnPath(cdn_server + basePath + "index.html");
                }
                // 解压到服务器目录
                ZipUtil.unZip(tempFile, test_path + basePath);
                boolean delete = tempFile.delete();
                log.info("本地压缩包已删除： " + delete);
            } catch (Exception e) {
                log.info(e.getMessage());
                throw new RuntimeException("上传文件异常:");
            }
            info.setName(name);
            info.setCode(code);
            info.setVersion(version);
            info.setLocalPath(test_url + basePath + "index.html");
            info.setCreateTime(date);
            info.setStatus(status);
            unityInfoMapper.insert(info);
        }
    }

    @Override
    public void uploadConfig(ConfigInputDTO inputDTO) {
        MultipartFile file = inputDTO.getConfigFile();
        String projectCode = inputDTO.getCode();
        if (null != file && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            if (!"config.json".equals(fileName)) {
                throw new RuntimeException("传入文件名有误,必须是config.json");
            }

            String filePath = test_path + "/" + projectCode + "/" + fileName;
            File targetFile = new File(filePath);
            // 保证这个文件的父文件夹必须要存在
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(targetFile, false))) {
                outputStream.write(file.getBytes());
            } catch (Exception e) {
                log.info(e.getMessage());
                throw new RuntimeException("Error occurred while writing the config file!", e);
            }
        }
    }

    @Override
    public void deleteUnity(Integer id) {
        UnityInfo unityInfo = unityInfoMapper.selectByPrimaryKey(id);
        String dir = test_path + unityInfo.getCode() + "/" + unityInfo.getVersion();
        try {
            FileUtil.deleteFile(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (unityInfo.getStatus() == Constant.STATUS_TEST) {
            unityInfoMapper.deleteByPrimaryKey(id);
        } else {
            unityInfo.setLocalPath("");
            unityInfoMapper.updateByPrimaryKeySelective(unityInfo);
        }
    }

    public Map<String, List<String>> versions(String code) {

        Example example = new Example(UnityInfo.class);
        example.and().andEqualTo("code", code);
        List<UnityInfo> unityInfos = unityInfoMapper.selectByExample(example);

        List<String> online = new ArrayList<>();
        List<String> test = new ArrayList<>();
        unityInfos.forEach(info -> {
            if (info.getStatus() == 2) {
                online.add(info.getVersion());
            } else {
                test.add(info.getVersion());
            }
        });

        Map<String, List<String>> map = new HashMap<>();
        map.put("online", online);
        map.put("test", test);

        return map;
    }
}
