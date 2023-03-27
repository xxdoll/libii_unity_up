package com.libii.sso.unity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@ApiModel("配置文件输入参数")
public class ConfigInputDTO {
    @ApiModelProperty(value = "配置文件", required = true)
    private MultipartFile configFile;

    @ApiModelProperty(value = "项目编码", required = true)
    private String code;
}
