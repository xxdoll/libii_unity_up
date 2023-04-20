package com.libii.sso.unity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author: fengchenxin
 * @ClassName: WhiteListInputDTO
 * @date: 2023-04-20  10:46
 * @Description: TODO
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhiteListInputDTO {

    @NotBlank
    private String gameId;
    @NotBlank
    private String deviceId;
}
