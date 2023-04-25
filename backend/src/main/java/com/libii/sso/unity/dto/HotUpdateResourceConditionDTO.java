package com.libii.sso.unity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author: fengchenxin
 * @ClassName: HotUpdateResourceConditionDTO
 * @date: 2023-04-24  10:17
 * @Description: TODO
 * @version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotUpdateResourceConditionDTO {

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
     *  unity版本
     */
    private String unityVersion;

}
