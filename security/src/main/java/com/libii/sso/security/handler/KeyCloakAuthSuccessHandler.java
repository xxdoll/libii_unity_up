package com.libii.sso.security.handler;

import com.alibaba.fastjson.JSON;
import com.libii.sso.common.config.IApplicationConfig;
import com.libii.sso.common.model.Constant;
import com.libii.sso.common.restResult.RestResult;
import com.libii.sso.common.util.IPUtils;
import com.libii.sso.security.util.ResponseUtils;
import com.libii.sso.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class KeyCloakAuthSuccessHandler extends KeycloakAuthenticationSuccessHandler {

    @Autowired
    private IApplicationConfig applicationConfig;

    public KeyCloakAuthSuccessHandler(AuthenticationSuccessHandler fallback) {
        super(fallback);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Object attribute = request.getSession().getAttribute(Constant.CACHE_REQUEST);
        if(null==attribute){
            super.onAuthenticationSuccess(request,response,authentication);
        } else {

            String employeeCode = SecurityUtils.getUsernameByAuth(authentication);

            //输出登录提示信息
            log.info("用户 " + employeeCode + " 登录");
            log.info("用户 " + JSON.toJSONString(authentication.getAuthorities()) + "角色权限");
            log.info("IP :"+ IPUtils.getIpAddr(request));

            // 记录登录成功的日志
            // JSON 格式的返回
            // ResponseUtils.renderSuccessJson(authentication, request, response, new RestResult(200, "登录成功", userAuthDTO), applicationConfig.getOrigins());

            //添加相应头信息并重定向到security缓存地址
            String location = attribute.toString();
            ResponseUtils.buildSuccessResponse(request,response,new RestResult(200, "登录成功", employeeCode),applicationConfig.getOrigins());
            String redirectUrl = location.substring(location.indexOf("[") + 1, location.indexOf("]"));
            response.sendRedirect(redirectUrl);

        }
    }
}