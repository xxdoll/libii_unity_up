package com.libii.sso.unity.service.impl;

import com.github.pagehelper.PageHelper;
import com.libii.sso.common.core.AbstractService;
import com.libii.sso.common.exception.CustomException;
import com.libii.sso.common.restResult.PageParam;
import com.libii.sso.common.restResult.ResultCode;
import com.libii.sso.unity.dao.DeviceInfoMapper;
import com.libii.sso.unity.dao.WhiteListMapper;
import com.libii.sso.unity.domain.DeviceInfo;
import com.libii.sso.unity.domain.WhiteListInfo;
import com.libii.sso.unity.dto.DeviceInfoDTO;
import com.libii.sso.unity.service.DeviceInfoService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author: fengchenxin
 * @ClassName: DeviceInfoServiceImpl
 * @date: 2023-04-20  11:06
 * @Description: TODO
 * @version: 1.0
 */
@Service
@Transactional
public class DeviceInfoServiceImpl extends AbstractService<DeviceInfo> implements DeviceInfoService {

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Resource
    private WhiteListMapper whiteListMapper;

    @Override
    public List<DeviceInfo> list(PageParam pageParam, DeviceInfoDTO queryDTO) {
        Example example = new Example(DeviceInfo.class);
        if(null!=queryDTO){
            if(StringUtils.hasText(queryDTO.getDeviceId())){
                example.and().andLike("deviceId", "%" + queryDTO.getDeviceId() + "%");
            }
            if(StringUtils.hasText(queryDTO.getDescription())){
                example.and().andLike("description", "%" + queryDTO.getDescription() + "%");
            }
        }
        example.setOrderByClause("create_time desc");
        PageHelper.startPage(pageParam.getPage(), pageParam.getSize());
        return deviceInfoMapper.selectByExample(example);
    }

    @Override
    public void add(DeviceInfo deviceInfo) {
        deviceInfo.setCreateTime(new Date());
        try {
            deviceInfoMapper.insert(deviceInfo);
        } catch (DuplicateKeyException e) {
            throw new CustomException(ResultCode.DEVICE_ID_EXIST);
        }
    }

    @Override
    public void deleteDevice(Integer id) {
        DeviceInfo deviceInfo = deviceInfoMapper.selectByPrimaryKey(id);
        Example example = new Example(WhiteListInfo.class);
        example.and().andEqualTo("deviceId",deviceInfo.getDeviceId());
        List<WhiteListInfo> whiteListInfos = whiteListMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(whiteListInfos)){
            whiteListMapper.deleteByDevice(deviceInfo.getDeviceId());
        }
        deviceInfoMapper.deleteByPrimaryKey(id);
    }


}
