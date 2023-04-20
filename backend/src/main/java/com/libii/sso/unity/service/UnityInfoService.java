package com.libii.sso.unity.service;

import com.libii.sso.common.core.Service;
import com.libii.sso.common.restResult.PageParam;
import com.libii.sso.unity.domain.UnityInfo;
import com.libii.sso.unity.dto.ConfigInputDTO;
import com.libii.sso.unity.dto.QueryUnityDTO;
import com.libii.sso.unity.dto.UnityInputDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Generate
 * @Description: // TODO 为类添加注释
 * @date 2021-03-01 03:18:38
 */
public interface UnityInfoService extends Service<UnityInfo> {

    /**
     * 根据分页、排序信息和检索条件查询 @size 条 字典表数据
     *
     * @param pageParam 分页参数
     * @param query     查询关键字
     * @return
     */
    List<UnityInfo> list(PageParam pageParam, QueryUnityDTO query);

    /**
     * 上传
     *
     * @param inputDTO
     */
    void uploadArchive(UnityInputDTO inputDTO);

    void uploadConfig(ConfigInputDTO inputDTO) throws Exception;

    void deleteUnity(Integer id);
    Map<String, List<String>> versions(String code);
}
