package com.libii.sso.security.entity;

import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecurityUser extends LoginUserAuthDTO implements UserDetails, Serializable {

    @Setter
    private Boolean accountNonExpired=true;
    @Setter
    private Boolean accountNonLocked=true;
    @Setter
    private Boolean credentialsNonExpired=true;
    @Setter
    private Boolean enabled=true;
    public SecurityUser (LoginUserAuthDTO user) {
        if(user != null) {
            BeanUtils.copyProperties(user, this);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<Role> userRoles = this.getRoles();
        if(userRoles != null){
            for (Role role : userRoles) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName());
                authorities.add(authority);
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return  super.getId().toString();
    }


    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}