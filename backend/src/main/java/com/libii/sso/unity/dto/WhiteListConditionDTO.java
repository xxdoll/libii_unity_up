package com.libii.sso.unity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: fengchenxin
 * @ClassName: WhiteListConditionDTO
 * @date: 2023-04-20  13:37
 * @Description: TODO
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhiteListConditionDTO {
    private String bundleId;

    private String deviceId;
}
