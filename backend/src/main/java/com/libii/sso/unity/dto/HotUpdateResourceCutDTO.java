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
public class HotUpdateResourceCutDTO {

    private Integer id;

    /**
     *  资源状态: -1-过时,0-审核,1-发布   内网默认发布，外网默认审核
     */
    private Integer status;

    /**
     *  资源服务器地址
     */
    private String serverUrl;

    /**
     *  地区(待定)
     */
    private String area;

    /**
     *  资源能够适配的最低应用版本号字符串
     */
    private String minAppVersionStr;

    /**
     *  资源能够适配的最低应用版本号整数
     */
    private Integer minAppVersionNum;

    /**
     *  资源能够适配的最高应用版本号字符串
     */
    private String maxAppVersionStr;

    /**
     *  资源能够适配的最高应用版本号整数
     */
    private Integer maxAppVersionNum;

    /**
     *  资源下载超时时间
     */
    private Integer timeout;

}
