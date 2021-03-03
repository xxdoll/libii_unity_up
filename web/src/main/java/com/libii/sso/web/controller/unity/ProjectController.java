package com.libii.sso.web.controller.unity;

import com.libii.sso.unity.domain.Project;
import com.libii.sso.unity.service.ProjectService;
import com.libii.sso.common.restResult.RestResult;
import com.libii.sso.common.restResult.ResultGenerator;
import com.libii.sso.common.restResult.PageParam;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
/**
*
* @author Generate
* @Description: // TODO 1. 为类添加注释
* @date 2021-03-03 03:50:52
*/
@Slf4j
@RestController
@RequestMapping("/project")
@Api(tags = "项目模块管理")
public class ProjectController {
    @Resource
    private ProjectService projectService;

    @PostMapping
    @ApiOperation(value = "新增项目", notes = "单个新增", produces = "application/json")
    public RestResult add(@ApiParam(name = "项目信息", required = true) @RequestBody Project project) {
        project.setCreateTime(new Date());
        projectService.save(project);
        return ResultGenerator.genSuccessResult().setMessage("保存成功");
    }

    @DeleteMapping
    @ApiOperation(value = "删除项目", notes = "单个删除", produces = "application/json")
    public RestResult delete(@ApiParam(name = "项目信息", required = true) Integer id) {
        projectService.deleteById(id);
        return ResultGenerator.genSuccessResult().setMessage("删除成功");
    }

    @PutMapping
    @ApiOperation(value = "修改项目", notes = "单个修改" , produces = "application/json")
    public RestResult update(@ApiParam(name = "项目信息", required = true) @RequestBody Project project) {
        projectService.update(project);
        return ResultGenerator.genSuccessResult().setMessage("修改成功");
    }

    /**
     * 用于分页查询,默认可以不用传分页信息
     * 默认值：page=1,size=10,sortField="id",sortOrder="ASC"
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "项目列表分页查询", notes = "分页列表", produces = "application/json")
    public RestResult<PageInfo> list(@ApiParam(value = "分页信息") PageParam pageParam,
                                     @ApiParam(value = "查询条件") @RequestParam(required = false, defaultValue = "") String query) {
        List<Project> list = projectService.list(pageParam, query);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 下拉框查询所有
     */
    @ApiOperation(value = "项目列表查询所有", notes = "下拉框列表", produces = "application/json")
    @GetMapping(value = "/all")
    public RestResult listAll() {
        List<Project> list = projectService.findAll();
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "项目字段校验", notes = "参数字段校验", produces = "application/json")
    @GetMapping(value = "/check")
    public RestResult keyIsExist(String domainField, String value) {
        Integer integer = projectService.keyIsExist(domainField, value);
        return ResultGenerator.genSuccessResult(integer);
    }

}
