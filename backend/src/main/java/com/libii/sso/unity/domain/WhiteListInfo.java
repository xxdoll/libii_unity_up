package com.libii.sso.unity.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author: fengchenxin
 * @ClassName: WhiteListInfo
 * @date: 2023-04-20  10:35
 * @Description: TODO
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game_device_info")
public class WhiteListInfo {
    @Id
    @Column(name = "game_id")
    private String gameId;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "create_time")
    private Date createTime;
}
