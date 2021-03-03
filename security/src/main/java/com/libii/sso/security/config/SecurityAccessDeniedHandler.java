package com.libii.sso.security.config;

import com.libii.sso.common.restResult.ResultCode;
import com.libii.sso.security.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户访问没有权限资源的处理
 * @author lirong
 * @date 2019-8-20 10:43:32
 */

@Component("securityAccessDeniedHandler")
@Slf4j
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException){
		log.info(request.getRequestURL()+"没有权限");
		ResponseUtils.renderJson(request, response, ResultCode.LIMITED_AUTHORITY, null);
	}
}
