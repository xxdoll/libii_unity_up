package com.libii.sso.security.provider;

import com.libii.sso.common.model.Constant;
import com.libii.sso.security.entity.LoginUserAuthDTO;
import com.libii.sso.security.entity.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("securityAuthenticationProvider")
@Slf4j
public class SecurityAuthenticationProvider implements AuthenticationProvider {

    @Value("${rpc.prefix.sso}")
    public String sso;
    @Autowired
    private KeycloakRestTemplate restTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws RuntimeException {
        // 从token中获取当前登录用户信息
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
        // Keycloak统一认证accesstoken放入消息头，获取SSO系统权限信息
        String authUrl = sso + Constant.AUTH_USER;
        ResponseEntity<LoginUserAuthDTO> exchange = restTemplate
                .exchange(authUrl,
                        HttpMethod.POST,
                        new HttpEntity<>(null, new HttpHeaders()),   //加入headers
                        LoginUserAuthDTO.class);
        LoginUserAuthDTO authDTO = exchange.getBody();
        SecurityUser securityUser = new SecurityUser(authDTO);
        // 封装授权后的 Authentication 对象
        KeycloakAuthenticationToken authenticationToken = new KeycloakAuthenticationToken(token.getAccount(), token.isInteractive(), mapAuthorities(securityUser.getAuthorities()));

        return authenticationToken;
    }


    private GrantedAuthoritiesMapper grantedAuthoritiesMapper;

    /**
     * 将存储中加载的权限转换为将在Authentication对象中使用的权限
     *
     * @param grantedAuthoritiesMapper
     */
    public void setGrantedAuthoritiesMapper(GrantedAuthoritiesMapper grantedAuthoritiesMapper) {
        this.grantedAuthoritiesMapper = grantedAuthoritiesMapper;
    }

    /**
     * 返回权限Collection集合转换方法（适用keycloak）
     *
     * @param authorities
     * @return
     */
    private Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return grantedAuthoritiesMapper != null ? grantedAuthoritiesMapper.mapAuthorities(authorities) : authorities;
    }

    /**
     * 开启keycloak   Token支持方式
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return KeycloakAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
