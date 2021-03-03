package com.libii.sso.user.service;

import com.libii.sso.user.domain.SsoUser;
import org.springframework.http.HttpEntity;

/**
* @author lirong
* @Description:
* @date 2019-10-09 10:12:18
*/
public interface UserService {

    /**
     * 获取登录用户
     * @param entity
     * @return
     */
    SsoUser getLoginUser(HttpEntity entity);
}
