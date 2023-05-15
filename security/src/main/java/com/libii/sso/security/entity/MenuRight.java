package com.libii.sso.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuRight implements Serializable {
    private Long id;

    private Long parentId;

    private String module;

    private String name;

    private Integer seq;

    private String url;

    private Integer status;

    private String icon;

    private String method;

    private Integer grades;

    private Date modifyTime;

    private Date createTime;
}