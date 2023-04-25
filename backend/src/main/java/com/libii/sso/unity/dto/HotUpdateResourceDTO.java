package com.libii.sso.unity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class HotUpdateResourceDTO {

    private Integer id;
    /**
     *  资源版本号
     */
    private String resourceVersion;

    /**
     *  资源状态: -1-过时,0-审核,1-发布   内网默认发布，外网默认审核
     */
    private Integer status;

    /**
     *  是否强制更新版本
     */
    private Boolean isForceUpdate;

    /**
     *  游戏ID
     */
    private String gameId;

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
     *  资源服务器地址
     */
    private String serverUrl;

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
     *  资源版本号
     */
    private String minAppVersionStr;

    /**
     *  资源版本号
     */
    private String maxAppVersionStr;

    /**
     *  资源版本号
     */
    private String minAppVersionNum;

    /**
     *  资源版本号
     */
    private String maxAppVersionNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
