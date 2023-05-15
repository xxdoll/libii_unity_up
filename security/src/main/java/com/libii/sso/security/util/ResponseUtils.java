package com.libii.sso.security.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.libii.sso.common.restResult.RestResult;
import com.libii.sso.common.restResult.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class ResponseUtils {

    public static void addResponseHeader(HttpServletRequest request, HttpServletResponse response, String[] origins) {
        String origin = request.getHeader("Origin");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader( "Access-Control-Allow-Headers", "Content-Type");
        // response.setHeader("Access-Control-Allow-Origin", "*");
        // if (Arrays.asList(origins).contains(origin)) {
        // todo 可以用 * 测试
        response.setHeader("Access-Control-Allow-Origin", origin);
        // }
    }

    public static void buildSuccessResponse(HttpServletRequest request, HttpServletResponse response, RestResult result, String[] origins){
        addResponseHeader(request, response, origins);
        response.setStatus(200);
    }


    public static void renderJson(HttpServletRequest request, HttpServletResponse response, ResultCode code, String[] origins) {
        renderJson(request, response, code, null, origins);
    }

    /**
     * 往 response 写出 json
     *
     * @param response 响应
     * @param code 状态
     * @param data     返回数据
     */
    public static void renderJson(HttpServletRequest request, HttpServletResponse response, ResultCode code, Object data, String[] origins) {
        try {
            addResponseHeader(request, response, origins);
            response.setStatus(200);
            //  将JSON转为String的时候，忽略null值的时候转成的String存在错误
            RestResult result = null != data ? new RestResult(code, data) : new RestResult(code);
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            log.error("Response写出JSON异常，", ex);
        }
    }

    /**
     * 往 response 写出 json
     *
     * @param response 响应
     * @param result 返回数据
     */
    public static void renderSuccessJson(Authentication authentication, HttpServletRequest request, HttpServletResponse response, RestResult result, String[] origins) {
        try {
            addResponseHeader(request, response, origins);
//            addAuthHeader(response,authentication);
            response.setStatus(200);
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            log.error("Response写出JSON异常，", ex);
        }
    }

    /**
     * 响应头携带IdToken信息，下次请求携带
     * @param response
     * @param authentication
     */
    public static void addAuthHeader( HttpServletResponse response,Authentication authentication){
        if(authentication instanceof KeycloakAuthenticationToken){
            KeycloakPrincipal principal = (KeycloakPrincipal)authentication.getPrincipal();
            String tokenString = principal.getKeycloakSecurityContext().getTokenString();
            response.setHeader("Authorization", "Bearer " +tokenString);
        }
    }


    //发给sso系统请求头
    public static HttpHeaders createAuthHttpHeader(String accessToken){
        HttpHeaders headers = createHttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }


    //封装header方法
    private static HttpHeaders createHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        //自定义报文头
        headers.setContentType(new MediaType("application","json",Charset.forName("utf-8")));
        //设置接收
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setAcceptCharset(Lists.newArrayList(Charset.forName("utf-8")));
        return headers;

    }
}
