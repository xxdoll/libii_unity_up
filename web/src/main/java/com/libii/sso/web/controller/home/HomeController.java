package com.libii.sso.web.controller.home;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@Api(tags = "通用登陆注销跳转控制")
public class HomeController {

    @Value("${auth-platforms}")
    private String auth_platforms;

    @Value("${auth-sso}")
    private String ssoUrl;

    /**
     * 首页跳转链接
     * @param response
     */
    @GetMapping("/platforms")
    @ApiOperation(value = "获取整合页地址", notes = "整合页地址", produces = "application/json")
    public void directToHome(HttpServletResponse response) {
        try {
            response.sendRedirect(auth_platforms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/logout")
    @ApiOperation(value = "获取整合页地址", notes = "整合页地址", produces = "application/json")
    public void logout(HttpServletResponse response) {
        try {
            response.sendRedirect(ssoUrl + "/logout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value={
            "/version-manage",
            "/switch-setting",
            "/time-line",
            "/account-query",
            "/base/**"
    })
    public void fowardRouter(HttpServletRequest request, HttpServletResponse response){
        try {
            request.getRequestDispatcher("/index.html").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
