package com.libii.sso.unity.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author: fengchenxin
 * @ClassName: DeviceInfo
 * @date: 2023-04-20  10:59
 * @Description: TODO
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device_info")
public class DeviceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "device_id")
    @NotBlank
    private String deviceId;

    @Column(name = "description")
    private String description;

    @Column(name = "create_time")
    private Date createTime;
}
