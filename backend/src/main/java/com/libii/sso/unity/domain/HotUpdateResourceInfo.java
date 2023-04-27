package com.libii.sso.unity.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

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
@Table(name = "hot_update_info")
public class HotUpdateResourceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     *  资源版本号
     */
    @Column(name = "resource_version")
    private String resourceVersion;

    /**
     *  资源状态: -1-过时,0-审核,1-发布   内网默认发布，外网默认审核
     */
    @Column(name = "status")
    private Integer status;

    /**
     *  是否强制更新版本 0-不强更 1-强更
     */
    @Column(name = "is_force_update")
    private Boolean isForceUpdate;

    /**
     *  游戏ID
     */
    @Column(name = "bundle_id")
    private String bundleId;

    /**
     *  平台
     */
    @Column(name = "platform")
    private String platform;

    /**
     *  渠道
     */
    @Column(name = "channel")
    private String channel;

    /**
     *  资源自定义的额外ID
     */
    @Column(name = "custom_id")
    private String customId;

    /**
     *  地区
     */
    @Column(name = "area")
    private String area;

    /**
     *  资源服务器地址
     */
    @Column(name = "server_url")
    private String serverUrl;

    /**
     *  unity版本号
     */
    @Column(name = "unity_version")
    private String unityVersion;

    /**
     *  app版本号字符串
     */
    @Column(name = "app_version_str")
    private String appVersionStr;

    /**
     *  app版本号int整数
     */
    @Column(name = "app_version_num")
    private Integer appVersionNum;

    /**
     *  资源能够适配的最低应用版本号字符串
     */
    @Column(name = "min_app_version_str")
    private String minAppVersionStr;

    /**
     *  资源能够适配的最高应用版本号字符串
     */
    @Column(name = "max_app_version_str")
    private String maxAppVersionStr;

    /**
     *  资源能够适配的最低应用版本号整数
     */
    @Column(name = "min_app_version_num")
    private Integer minAppVersionNum;

    /**
     *  资源能够适配的最高应用版本号整数
     */
    @Column(name = "max_app_version_num")
    private Integer maxAppVersionNum;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

}
