package com.libii.sso.user.domain;

import lombok.Data;

/**
 * @author lirong
 * @ClassName: SSOParamDTO
 * @Description: 用户相关查询参数
 * @date 2019-09-02 11:19
 */
@Data
public class SSOParamDTO {

    private String username;

    private String actualName;

    private Integer status;

    private Integer page = 1;

    private Integer size = 10;

    private String sortField = "id";

    private String sortOrder = "DESC";
}
