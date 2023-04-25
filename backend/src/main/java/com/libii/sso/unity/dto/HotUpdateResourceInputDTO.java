package com.libii.sso.unity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: fengchenxin
 * @ClassName: HotUpdateResourceDTO
 * @date: 2023-04-23  15:24
 * @Description: TODO
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotUpdateResourceInputDTO {

    /**
     *  资源版本号
     */
    @NotBlank
    private String resourceVersion;

    /**
     *  游戏ID
     */
    @NotBlank
    private String gameId;

    /**
     *  平台
     */
    @NotBlank
    private String platform;

    /**
     *  渠道
     */
    @NotBlank
    private String channel;

    /**
     *  资源自定义的额外ID
     */
    @NotBlank
    private String customId;

    /**
     *  unity版本号
     */
    @NotBlank
    private String unityVersion;

    /**
     *  app版本号字符串
     */
    @NotBlank
    private String appVersionStr;

    /**
     *  app版本号int整数
     */
    @NotNull
    private Integer appVersionNum;

    /**
     * 资源.zip文件
     */
    @NotNull
    private MultipartFile resource;


}
