package com.libii.sso.unity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询条件")
public class QueryUnityDTO {

    @ApiModelProperty("项目编码")
    private String code;

    @ApiModelProperty("版本号")
    private String version;

    @ApiModelProperty("状态")
    private Integer status;
}
