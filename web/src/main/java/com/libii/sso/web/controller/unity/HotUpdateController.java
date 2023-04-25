package com.libii.sso.web.controller.unity;

import com.github.pagehelper.PageInfo;
import com.libii.sso.common.restResult.PageParam;
import com.libii.sso.common.restResult.RestResult;
import com.libii.sso.common.restResult.ResultGenerator;
import com.libii.sso.unity.domain.HotUpdateResourceInfo;
import com.libii.sso.unity.dto.HotUpdateResourceConditionDTO;
import com.libii.sso.unity.dto.HotUpdateResourceCutDTO;
import com.libii.sso.unity.dto.HotUpdateResourceInputDTO;
import com.libii.sso.unity.dto.HotUpdateResourceQueryDTO;
import com.libii.sso.unity.service.HotUpdateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: fengchenxin
 * @ClassName: HotUpdateController
 * @date: 2023-04-23  14:18
 * @Description: TODO
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/hot-update")
@Api(tags = "热更新资源管理")
public class HotUpdateController {

    @Resource
    private HotUpdateService hotUpdateService;

    @PostMapping(value = "/upload")
    @ApiOperation(value = "上传热更新资源", notes = "上传热更新资源", produces = "application/json")
    public RestResult upload(@ApiParam(name = "上传参数") @Validated HotUpdateResourceInputDTO inputDTO) {
        try {
            hotUpdateService.uploadResource(inputDTO);
        } catch (Exception e) {
            return ResultGenerator.genFailResult(e.getMessage());
        }
        return ResultGenerator.genSuccessResult().setMessage("上传成功");
    }

    @ApiOperation(value = "获取热更新资源版本号", notes = "获取热更新资源版本号", produces = "application/json")
    @GetMapping(value = "/resource")
    public RestResult<Map<String, Object>> queryResource(HotUpdateResourceQueryDTO queryDTO) {
        Map<String, Object> envResourceInfo = hotUpdateService.queryResource(queryDTO);
        return ResultGenerator.genSuccessResult(envResourceInfo);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "热更新资源列表分页查询", notes = "分页列表", produces = "application/json")
    public RestResult<PageInfo> list(@ApiParam(value = "分页信息") PageParam pageParam,
                                     @ApiParam(value = "查询条件") HotUpdateResourceConditionDTO conditionDTO) {
        List<HotUpdateResourceInfo> list = hotUpdateService.list(pageParam, conditionDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PutMapping
    @ApiOperation(value = "修改热更新资源", notes = "单个修改" , produces = "application/json")
    public RestResult cut(@ApiParam(name = "热更新资源信息", required = true) @RequestBody HotUpdateResourceCutDTO cutDTO) {
        hotUpdateService.cut(cutDTO);
        return ResultGenerator.genSuccessResult().setMessage("修改成功");
    }

    @DeleteMapping
    @ApiOperation(value = "删除热更新资源", notes = "单个删除", produces = "application/json")
    public RestResult delete(@ApiParam(name = "热更新资源信息", required = true) Integer id) {
        hotUpdateService.deleteResource(id);
        return ResultGenerator.genSuccessResult().setMessage("删除成功");
    }

    @GetMapping(value = "/tree")
    @ApiOperation(value = "热更新资源分夜查询筛选条件层级下拉", notes = "热更新资源分夜查询筛选条件层级下拉", produces = "application/json")
    public RestResult<Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>>> queryTree() {
        Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> treeMap = hotUpdateService.queryTree();
        return ResultGenerator.genSuccessResult(treeMap);
    }

    @GetMapping(value = "/app-version")
    @ApiOperation(value = "修改热更新资源最高最低版本下拉", notes = "修改热更新资源最高最低版本下拉", produces = "application/json")
    public RestResult<List<Map<String,Integer>>> queryVersion(HotUpdateResourceConditionDTO conditionDTO) {
        List<Map<String,Integer>> list = hotUpdateService.queryVersion(conditionDTO);
        return ResultGenerator.genSuccessResult(list);
    }
}