package com.libii.sso.security.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
@Builder
public class LoginUserAuthDTO implements Serializable {


    /**
     * 用户id
     */
    private Long id;
    /**
     * 角色
     */
    private List<Role> roles;

    /**
     * 权限菜单
     */
    private List<MenuRight> menus;


    public LoginUserAuthDTO(Long id, List<Role> roles, List<MenuRight> menus) {
        this.id = id;
        this.roles = roles;
        this.menus = menus;
    }
}
