package com.libii.sso.web.controller.unity;

import com.github.pagehelper.PageInfo;
import com.libii.sso.common.restResult.PageParam;
import com.libii.sso.common.restResult.RestResult;
import com.libii.sso.common.restResult.ResultGenerator;
import com.libii.sso.unity.domain.DeviceInfo;
import com.libii.sso.unity.dto.DeviceInfoDTO;
import com.libii.sso.unity.service.DeviceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: fengchenxin
 * @ClassName: DeviceInfoController
 * @date: 2023-04-20  11:03
 * @Description: TODO
 * @version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/device")
@Api(tags = "设备管理")
public class DeviceInfoController {

    @Resource
    private DeviceInfoService deviceService;

    /**
     * 用于分页查询,默认可以不用传分页信息
     * 默认值：page=1,size=10,sortField="id",sortOrder="ASC"
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "设备列表分页查询", notes = "分页列表", produces = "application/json")
    public RestResult<PageInfo> list(@ApiParam(value = "分页信息") PageParam pageParam,
                                     @ApiParam(value = "查询条件") DeviceInfoDTO queryDTO) {
        List<DeviceInfo> list = deviceService.list(pageParam, queryDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping(value = "/all")
    @ApiOperation(value = "所有设备", notes = "分页列表", produces = "application/json")
    public RestResult all() {
        List<DeviceInfo> list = deviceService.findAll();
        return ResultGenerator.genSuccessResult(list);
    }

    @PostMapping
    @ApiOperation(value = "新增设备", notes = "单个新增", produces = "application/json")
    public RestResult add(@ApiParam(name = "设备信息", required = true) @RequestBody @Validated DeviceInfo deviceInfo) {
        deviceService.add(deviceInfo);
        return ResultGenerator.genSuccessResult().setMessage("保存成功");
    }

    @DeleteMapping
    @ApiOperation(value = "删除设备", notes = "单个删除", produces = "application/json")
    public RestResult delete(@ApiParam(name = "设备信息", required = true) Integer id) {
        deviceService.deleteDevice(id);
        return ResultGenerator.genSuccessResult().setMessage("删除成功");
    }
}
