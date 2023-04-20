package com.libii.sso.unity.service;

import com.libii.sso.common.core.Service;
import com.libii.sso.common.restResult.PageParam;
import com.libii.sso.unity.domain.DeviceInfo;
import com.libii.sso.unity.dto.DeviceInfoDTO;

import java.util.List;

public interface DeviceInfoService extends Service<DeviceInfo> {

    List<DeviceInfo> list(PageParam pageParam, DeviceInfoDTO queryDTO);

    void add(DeviceInfo deviceInfo);

    void deleteDevice(Integer id);
}
