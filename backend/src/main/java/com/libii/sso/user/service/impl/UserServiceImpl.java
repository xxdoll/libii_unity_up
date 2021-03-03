package com.libii.sso.user.service.impl;

import com.libii.sso.user.domain.SsoUser;
import com.libii.sso.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
* @author lirong
* @Description: 用户
* @date 2019-10-09 10:12:18
*/
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${auth-server}")
    public String auth_server;

    /**
     * 获取登录用户
     * @param entity
     * @return
     */
    @Override
    public SsoUser getLoginUser(HttpEntity entity) {
        String url = auth_server + "/user/oauth/sso";
        return restTemplate.postForObject(url, entity, SsoUser.class);
    }
}
