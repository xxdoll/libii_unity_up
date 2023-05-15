package com.libii.sso.unity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: fengchenxin
 * @ClassName: HotUpdateResourceInfo
 * @date: 2023-04-23  14:38
 * @Description: TODO
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotUpdateInetInputDTO {

    /**
     *  资源版本号
     */
    private String resourceVersion;

    /**
     *  游戏ID
     */
    private String bundleId;

    /**
     *  游戏名
     */
    private String name;

    /**
     *  平台
     */
    private String platform;

    /**
     *  渠道
     */
    private String channel;

    /**
     *  资源自定义的额外ID
     */
    private String customId;

    /**
     *  地区
     */
    private String area;

    /**
     *  unity版本号
     */
    private String unityVersion;

    /**
     *  app版本号字符串
     */
    private String appVersionStr;

    /**
     *  app版本号int整数
     */
    private Integer appVersionNum;

    /**
     *  资源能够适配的最低应用版本号字符串
     */
    private String minAppVersionStr;

    /**
     *  资源能够适配的最高应用版本号字符串
     */
    private String maxAppVersionStr;

    /**
     *  资源能够适配的最低应用版本号整数
     */
    private Integer minAppVersionNum;

    /**
     *  资源能够适配的最高应用版本号整数
     */
    private Integer maxAppVersionNum;

    /**
     *  资源下载超时时间
     */
    private Integer timeout;

}
