package com.libii.sso.unity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: fengchenxin
 * @ClassName: WhiteListOutputDTO
 * @date: 2023-04-20  11:42
 * @Description: TODO
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhiteListOutputDTO {

    private String gameId;

    private String deviceId;

    private String description;

    private Date createTime;
}
