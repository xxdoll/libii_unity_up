package com.libii.sso.unity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

/**
 * @author: fengchenxin
 * @ClassName: HotUpdateResourceOutpDTO
 * @date: 2023-04-24  10:23
 * @Description: TODO
 * @version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotUpdateResourceOutpDTO {

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
     *  是否强制更新版本 0-不强更 1-强更
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
     *  资源能够适配的最低应用版本号字符串
     */
    private String minAppVersionStr;

    /**
     *  资源能够适配的最高应用版本号字符串
     */
    private String maxAppVersionStr;

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
