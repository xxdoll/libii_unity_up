package com.libii.sso.unity.domain;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "unity_info")
public class UnityInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 项目编码
     */
    private String code;

    /**
     * 版本号
     */
    private String version;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 状态：1-测试，2-正式
     */
    private Integer status;

    /**
     * 测试服预览路径
     */
    @Column(name = "local_path")
    private String localPath;

    /**
     * cdn预览路径
     */
    @Column(name = "cdn_path")
    private String cdnPath;

    /**
     * 上传日期
     */
    @Column(name = "create_time")
    private Date createTime;

}