package com.libii.sso.unity.dao;

import com.libii.sso.common.core.Mapper;
import com.libii.sso.unity.domain.UnityInfo;

import java.util.List;

public interface UnityInfoMapper extends Mapper<UnityInfo> {

    List<String> getAllVersions(String code);

}