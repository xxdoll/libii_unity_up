package com.libii.sso.security.util;


import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Principal;

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
     * 获取原生Authentication对象
     * @return
     */
    public static Authentication getAuth(){
        SecurityContext context = SecurityContextHolder.getContext();
        if(context==null){
            return null;
        }
        return context.getAuthentication();
    }

    /**
     * 获取KeycloakSecurityContext
     * -包括idToken  accessToken   授权信息
     * -提供token解析 praseToken
     *  @return
     */
    public static KeycloakSecurityContext getKeycloakSecurityContext(){
        if(getAuth() instanceof KeycloakAuthenticationToken){
            KeycloakAuthenticationToken token = (KeycloakAuthenticationToken)getAuth();
            KeycloakSecurityContext keycloakSecurityContext = token.getAccount().getKeycloakSecurityContext();
            return keycloakSecurityContext;
        }
        return null;
    }
    /**
     * 获取当前登录用户信息 AccessToken
     *  基于keycloak
     * @param token
     * @return
     */
    public static AccessToken getAccessToken(Authentication token) {
        Object principal = token.getPrincipal();
        if (principal instanceof KeycloakPrincipal) {
            KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) principal;
            KeycloakSecurityContext context = keycloakPrincipal.getKeycloakSecurityContext();
            return context.getToken();
        }
        return null;
    }

    /**
     * Authentication中获取用户名
     */
    public static String getUsernameByAuth(Authentication authentication){
        AccessToken accessToken = SecurityUtils.getAccessToken(authentication);
        return accessToken.getPreferredUsername();
    }


    /**
     * 获取keycloak请求格式用户id
     * @return
     */
    public static String getLoginUserId(){
        if(null==getAuth()){
            return null;
        }
        Object details = getAuth().getDetails();
        if (details instanceof SimpleKeycloakAccount){
            SimpleKeycloakAccount detail =(SimpleKeycloakAccount) details;
            Principal principal = detail.getPrincipal();
            if(principal instanceof KeycloakPrincipal) {
                AccessToken accessToken = ((KeycloakPrincipal) principal).getKeycloakSecurityContext().getToken();
                String preferredUsername = accessToken.getPreferredUsername();
                return preferredUsername;
            }
        }
        return null;
    }

    /**
     * 获取向sso发送的access token
     * @return
     */
    public static String getAccessToken(){
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken)getAuth();
        KeycloakSecurityContext keycloakSecurityContext = token.getAccount().getKeycloakSecurityContext();
        String tokenString = keycloakSecurityContext.getTokenString();
        return tokenString;
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
        String idToken = getIdToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + idToken);
        return headers;
    }

    /**
     *  基于keycloak获取与认证中心交互的token
     * @return jwtToken
     */
    public static String getIdToken() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if(authentication instanceof KeycloakAuthenticationToken){
            SimpleKeycloakAccount detail = (SimpleKeycloakAccount)authentication.getDetails();
            String idTokenString = detail.getKeycloakSecurityContext().getIdTokenString();
            return idTokenString;
        }
        return null;
    }

    /**
     * 功能：返回当前用户<br/>   原生security获取用户
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
