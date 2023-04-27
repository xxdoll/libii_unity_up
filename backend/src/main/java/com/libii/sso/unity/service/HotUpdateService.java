package com.libii.sso.unity.service;

import com.libii.sso.common.core.Service;
import com.libii.sso.common.restResult.PageParam;
import com.libii.sso.unity.domain.HotUpdateResourceInfo;
import com.libii.sso.unity.dto.*;

import java.util.List;
import java.util.Map;

public interface HotUpdateService extends Service<HotUpdateResourceInfo> {

    void uploadResource(HotUpdateResourceInputDTO inputDTO);

    Map<String, Object> queryResource(HotUpdateResourceQueryDTO queryDTO);

    Map<String, Object> queryTestResource(HotUpdateResourceQueryDTO queryDTO);

    List<HotUpdateResourceInfo> list(PageParam pageParam, HotUpdateResourceConditionDTO conditionDTO);

    void cut(HotUpdateResourceCutDTO cutDTO);

    void deleteResource(Integer id);

    Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> queryTree();

    List<Map<String,Integer>> queryVersion(HotUpdateResourceConditionDTO conditionDTO);

    void pushResourceConfig(Integer id);

    void consumeTestConfig(HotUpdateInetInputDTO inputDTO);

    List<String> queryRepositoryUrl();
}
