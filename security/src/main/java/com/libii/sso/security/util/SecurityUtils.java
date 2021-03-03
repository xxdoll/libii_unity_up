package com.libii.sso.security.util;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SecurityUtils {

    /**
     * 判断当前用户是否已经登陆
     * @return 登陆状态返回 true, 否则返回 false
     */
    public static boolean isLogin() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return !"anonymousUser".equals(username);
    }

    /**
     * 构建 与认证中心交互的 entity
     * @return
     */
    public static HttpEntity buildParam(){
        return new HttpEntity<>(getHttpHeaders());
    }

    /**
     * 构建 与认证中心交互的 headers
     * @return
     */
    public static HttpHeaders getHttpHeaders(){
        String tokenValue = SecurityUtils.getJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + tokenValue);
        return headers;
    }

    /**
     * 获取与认证中心交互的token
     * @return jwtToken
     */
    public static String getJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        return details.getTokenValue();
    }

    /**
     * 功能：返回当前用户<br/>
     * @return
     * @exception
     */
    public static String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication) {
            Object principal = authentication.getPrincipal();
            if (!StringUtils.isEmpty(principal) && !"anonymousUser".equals(principal)){
                return (String) principal;
            }
        }
        return null;
    }
}
