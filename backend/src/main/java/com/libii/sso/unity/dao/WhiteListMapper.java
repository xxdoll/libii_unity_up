package com.libii.sso.unity.dao;

import com.libii.sso.common.core.Mapper;
import com.libii.sso.unity.domain.WhiteListInfo;
import com.libii.sso.unity.dto.WhiteListConditionDTO;
import com.libii.sso.unity.dto.WhiteListInputDTO;
import com.libii.sso.unity.dto.WhiteListOutputDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WhiteListMapper extends Mapper<WhiteListInfo> {

    List<WhiteListOutputDTO> list(@Param("dto") WhiteListConditionDTO whiteListConditionDTO);

    void deleteByDevice(@Param("deviceId") String deviceId);

    void deleteWhiteList(@Param("dto") WhiteListInputDTO inputDTO);
}
