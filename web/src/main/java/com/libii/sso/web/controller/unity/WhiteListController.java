package com.libii.sso.web.controller.unity;

import com.github.pagehelper.PageInfo;
import com.libii.sso.common.restResult.PageParam;
import com.libii.sso.common.restResult.RestResult;
import com.libii.sso.common.restResult.ResultGenerator;
import com.libii.sso.unity.dto.WhiteListConditionDTO;
import com.libii.sso.unity.dto.WhiteListInputDTO;
import com.libii.sso.unity.dto.WhiteListOutputDTO;
import com.libii.sso.unity.service.WhiteListService;
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
 * @ClassName: WhiteListController
 * @date: 2023-04-20  10:22
 * @Description: TODO
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/white-list")
@Api(tags = "内网设备白名单管理")
public class WhiteListController {

    @Resource
    private WhiteListService whiteListService;

    /**
     * 用于分页查询,默认可以不用传分页信息
     * 默认值：page=1,size=10,sortField="id",sortOrder="ASC"
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "白名单分页查询", notes = "分页列表", produces = "application/json")
    public RestResult<PageInfo> list(@ApiParam(value = "分页信息") PageParam pageParam,
                                     @ApiParam(value = "查询条件") WhiteListConditionDTO whiteListConditionDTO) {
        List<WhiteListOutputDTO> list = whiteListService.list(pageParam, whiteListConditionDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping
    @ApiOperation(value = "白名单添加", notes = "新增", produces = "application/json")
    public RestResult addWhiteList(@ApiParam(name = "新增白名单")@RequestBody @Validated WhiteListInputDTO inputDTO) {
        whiteListService.add(inputDTO);
        return ResultGenerator.genSuccessResult().setMessage("白名单添加成功");
    }

    @DeleteMapping
    @ApiOperation(value = "白名单删除", notes = "删除", produces = "application/json")
    public RestResult deleteWhiteList(@ApiParam(name = "删除白名单") WhiteListInputDTO inputDTO) {
        whiteListService.deleteWhiteList(inputDTO);
        return ResultGenerator.genSuccessResult().setMessage("白名单删除成功");
    }

    @GetMapping(value = "/verify")
    @ApiOperation(value = "白名单校验", notes = "分页列表", produces = "application/json")
    public RestResult verify(@Validated WhiteListInputDTO whiteListInputDTO) {
        Map<String,Integer> result = whiteListService.verify( whiteListInputDTO);
        return ResultGenerator.genSuccessResult(result);
    }
}
