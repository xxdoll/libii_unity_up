package com.libii.sso.web.controller.user;

import com.libii.sso.common.restResult.RestResult;
import com.libii.sso.common.restResult.ResultGenerator;
import com.libii.sso.security.util.SecurityUtils;
import com.libii.sso.user.domain.SSOParamDTO;
import com.libii.sso.user.domain.SsoUser;
import com.libii.sso.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
/**
*
* @author lirong
* @Description: 用户
* @date 2019-10-09 10:12:18
*/
@Slf4j
@RestController
@RequestMapping("/sso")
@Api(tags = "用户模块管理")
public class UserController {
    @Resource
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth-server}")
    public String auth_server;
    @Value("${auth-platforms}")
    public String auth_platforms;
    @Value("${base-data-server}")
    public String base_data_server;

    @GetMapping("/platforms/url")
    @ApiOperation(value = "获取整合页地址", notes = "整合页地址", produces = "application/json")
    public RestResult getSSOServerUrl() {
        return ResultGenerator.genSuccessResult(auth_platforms);
    }

    @GetMapping("/user/info")
    @ApiOperation(value = "获取登录用户", notes = "获取登录用户", produces = "application/json")
    public RestResult<SsoUser> getLoginUser() {
        HttpEntity entity = SecurityUtils.buildParam();
        SsoUser user = userService.getLoginUser(entity);
        return ResultGenerator.genSuccessResult(user);
    }

    @GetMapping("/user/list")
    @ApiOperation(value = "获取分页用户列表", notes = "获取分页用户列表，size=0则不分页", produces = "application/json")
    public RestResult getLoginUserList(SSOParamDTO dto) {
        String url = auth_server + "/user/oauth/list";
        HttpHeaders headers = SecurityUtils.getHttpHeaders();
        HttpEntity entity = new HttpEntity<>(dto, headers);
        RestResult result = restTemplate.postForObject(url, entity, RestResult.class);
        return result;
    }

    /**
     * 获取 用户拥有的菜单
     * @return
     */
    @GetMapping("/user/systemMenu")
    @ApiOperation(value = "获取用户菜单", notes = "获取用户菜单", produces = "application/json")
    public RestResult getUserSystemMenus(Long moduleId) {
        String url = auth_server + "/user/oauth/systemMenu?moduleId=" + moduleId;
        HttpEntity entity = SecurityUtils.buildParam();
        RestResult result = restTemplate.postForObject(url, entity, RestResult.class);
        return result;
    }

    @GetMapping("/channel/all")
    @ApiOperation(value = "获取系统渠道列表", notes = "下拉框", produces = "application/json")
    public RestResult channelList() {
        String url = base_data_server + "/channel/all";
        return restTemplate.getForObject(url, RestResult.class);
    }

    @GetMapping("/app/all")
    @ApiOperation(value = "获取系统游戏列表", notes = "下拉框", produces = "application/json")
    public RestResult userAppList() {
        String url = base_data_server + "/app/all";
        return restTemplate.getForObject(url, RestResult.class);
    }
}
