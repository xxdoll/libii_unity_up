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
public class Role implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Integer seq;

    private Integer status;

    private Date createTime;

    private Date modifyTime;
}