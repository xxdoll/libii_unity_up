package com.libii.sso.unity.service;

import com.libii.sso.common.core.Service;
import com.libii.sso.common.restResult.PageParam;
import com.libii.sso.unity.domain.WhiteListInfo;
import com.libii.sso.unity.dto.WhiteListConditionDTO;
import com.libii.sso.unity.dto.WhiteListInputDTO;
import com.libii.sso.unity.dto.WhiteListOutputDTO;

import java.util.List;
import java.util.Map;

/**
 * @author: fengchenxin
 * @ClassName: WhiteListService
 * @date: 2023-04-20  10:34
 * @Description: TODO
 * @version: 1.0
 */
public interface WhiteListService extends Service<WhiteListInfo> {

    List<WhiteListOutputDTO> list(PageParam pageParam, WhiteListConditionDTO whiteListConditionDTO);

    Map<String, Integer> verify(WhiteListInputDTO whiteListInputDTO);

    void add(WhiteListInputDTO inputDTO);

    void deleteWhiteList(WhiteListInputDTO inputDTO);
}
