package com.libii.sso.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户未登录时的处理
 * @author lirong
 * @date 2019-8-8 17:37:27
 */
//@Component("securityAuthenticationEntryPoint")
@Slf4j
@Deprecated
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		log.info("尚未登录:" + authException.getMessage());
		response.sendRedirect(request.getContextPath() + "/login");

		// ResponseUtils.renderJson(request, response, ResultCode.UNLOGIN, null);
	}
}
