package com.libii.sso.web.controller.unity;

import com.libii.sso.unity.domain.UnityInfo;
import com.libii.sso.unity.dto.QueryUnityDTO;
import com.libii.sso.unity.dto.UnityInputDTO;
import com.libii.sso.unity.service.UnityInfoService;
import com.libii.sso.common.restResult.RestResult;
import com.libii.sso.common.restResult.ResultGenerator;
import com.libii.sso.common.restResult.PageParam;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
/**
*
* @author Generate
* @Description:
* @date 2021-03-01 03:18:38
*/
@Slf4j
@RestController
@RequestMapping("/unity")
@Api(tags = "unity资源模块管理")
public class UnityInfoController {
    @Resource
    private UnityInfoService unityInfoService;

    @PostMapping(value = "/upload")
    @ApiOperation(value = "上传unity压缩包", notes = "上传" , produces = "application/json")
    public RestResult upload(@ApiParam(name = "上传参数") UnityInputDTO inputDTO) {
        unityInfoService.upload(inputDTO);
        return ResultGenerator.genSuccessResult().setMessage("上传成功");
    }

    @PostMapping
    @ApiOperation(value = "新增unity资源", notes = "单个新增", produces = "application/json")
    public RestResult add(@ApiParam(name = "unity资源信息", required = true) @RequestBody UnityInfo unityInfo) {
        unityInfo.setCreateTime(new Date());
        unityInfoService.save(unityInfo);
        return ResultGenerator.genSuccessResult().setMessage("保存成功");
    }

    @DeleteMapping
    @ApiOperation(value = "删除unity资源", notes = "单个删除", produces = "application/json")
    public RestResult delete(@ApiParam(name = "unity资源信息", required = true) Integer id) {
        unityInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult().setMessage("删除成功");
    }

    @PutMapping
    @ApiOperation(value = "修改unity资源", notes = "单个修改" , produces = "application/json")
    public RestResult update(@ApiParam(name = "unity资源信息", required = true) @RequestBody UnityInfo unityInfo) {
        unityInfoService.update(unityInfo);
        return ResultGenerator.genSuccessResult().setMessage("修改成功");
    }

    @GetMapping
    @ApiOperation(value = "获取unity资源信息", notes = "单个获取", produces = "application/json")
    public RestResult<UnityInfo> detail(@ApiParam(value = "主键ID") @RequestParam Integer id) {
        UnityInfo unityInfo = unityInfoService.findById(id);
        return ResultGenerator.genSuccessResult(unityInfo);
    }

    /**
     * 用于分页查询,默认可以不用传分页信息
     * 默认值：page=1,size=10,sortField="id",sortOrder="ASC"
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "unity资源列表分页查询", notes = "分页列表", produces = "application/json")
    public RestResult<PageInfo> list(@ApiParam(value = "分页信息") PageParam pageParam,
                                     @ApiParam(value = "查询条件") QueryUnityDTO queryDTO) {
        List<UnityInfo> list = unityInfoService.list(pageParam, queryDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 下拉框查询所有
     */
    @ApiOperation(value = "unity资源列表查询所有", notes = "下拉框列表", produces = "application/json")
    @GetMapping(value = "/all")
    public RestResult listAll() {
        List<UnityInfo> list = unityInfoService.findAll();
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "unity资源字段校验", notes = "参数字段校验", produces = "application/json")
    @GetMapping(value = "/check")
    public RestResult keyIsExist(String domainField, String value) {
        Integer integer = unityInfoService.keyIsExist(domainField, value);
        return ResultGenerator.genSuccessResult(integer);
    }

}
