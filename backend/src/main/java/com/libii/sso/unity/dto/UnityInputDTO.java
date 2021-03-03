package com.libii.sso.unity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@ApiModel("新增输入参数")
public class UnityInputDTO {
    @ApiModelProperty("zip文件")
    private MultipartFile zipFile;

    @ApiModelProperty("项目编码")
    private String code;

    @ApiModelProperty("项目名称")
    private String name;

    @ApiModelProperty("状态")
    private Integer status;
}
