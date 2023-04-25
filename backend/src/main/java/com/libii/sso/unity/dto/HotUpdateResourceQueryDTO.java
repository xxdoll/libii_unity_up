package com.libii.sso.unity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class HotUpdateResourceQueryDTO {

    /**
     *  本地资源版本
     */
    private String resourceVersion;

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
     *  unity版本号
     */
    private String unityVersion;

    /**
     *  app版本号int整数
     */
    private Integer appVersionNum;

    /**
     * 设备Id
     */
    private String deviceId;

}
