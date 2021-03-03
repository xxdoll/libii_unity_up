package com.libii.sso.security.config;

import com.google.common.collect.Sets;
import com.libii.sso.common.model.Constant;
import com.libii.sso.security.ignore.CustomConfig;
import com.libii.sso.security.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lirong
 * @ClassName: UrlFilterInvocationSecurityMetadataSource
 * @Description: TODO
 * @date 2019-07-10 14:36
 */
@Component("urlFilterInvocationSecurityMetadataSource")
@Slf4j
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    /**
     * 当前激活的配置文件
     */
    @Value("${spring.profiles.active}")
    private String env;
    @Value("${client.moduleId}")
    private String moduleId;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CustomConfig customConfig;
    private static final long delayTime = 600L;
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        // 开发环境不启用权限验证
        if (env.contains("dev")) {
            return null;
        }

        HttpServletRequest request = ((FilterInvocation) o).getHttpRequest();
        HttpServletResponse response = ((FilterInvocation) o).getHttpResponse();
//        if (!env.contains("prod")) {
//            // 添加来自整合页面的跨域请求头
//            ResponseUtils.addResponseHeader(request, response, null);
//        }
        // 检查是否为放行的请求
        if (checkIgnores(request)){
            return null;
        }
        // 获取Redis中用户的登录信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<Map<String, String[]>> menuMap = (List<Map<String, String[]>>) redisTemplate.opsForValue().get(Constant.REDIS_PERM_KEY_PREFIX + username);
        // 判断此用户是否登录，是否在其他客户端退出
        if(null == menuMap){
            throw new AccountExpiredException("用户已在其他客户端退出 或 未登录");
        }
        // 更新redis的保存时间
        Long expireTime = redisTemplate.getExpire(Constant.REDIS_PERM_KEY_PREFIX + username);
        if (expireTime < delayTime){
            // 过期时间 = 当前剩余过期时间 + 600s
            redisTemplate.expire(Constant.REDIS_PERM_KEY_PREFIX + username,expireTime + delayTime , TimeUnit.SECONDS);
        }
        for (Map<String, String[]> map : menuMap) {
            for (String url : map.keySet()) {
                String[] split = url.split(":");
                if (!Objects.equals(split[0], moduleId)){
                    continue;
                }
                AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(split[2], split[1]);
                if(antPathMatcher.matches(request)){
                    return SecurityConfig.createList(map.get(url));
                }
            }
        }
        //没有匹配上的资源，都是登录访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    /**
     * 请求是否不需要进行权限拦截
     *
     * @param request 当前请求
     * @return true - 忽略，false - 不忽略
     */
    private boolean checkIgnores(HttpServletRequest request) {
        String method = request.getMethod();

        HttpMethod httpMethod = HttpMethod.resolve(method);
        if (null == httpMethod) {
            httpMethod = HttpMethod.GET;
        }

        Set<String> ignores = Sets.newHashSet();

        switch (httpMethod) {
            case GET:
                ignores.addAll(customConfig.getIgnores()
                        .getGet());
                break;
            case PUT:
                ignores.addAll(customConfig.getIgnores()
                        .getPut());
                break;
            case HEAD:
                ignores.addAll(customConfig.getIgnores()
                        .getHead());
                break;
            case POST:
                ignores.addAll(customConfig.getIgnores()
                        .getPost());
                break;
            case PATCH:
                ignores.addAll(customConfig.getIgnores()
                        .getPatch());
                break;
            case TRACE:
                ignores.addAll(customConfig.getIgnores()
                        .getTrace());
                break;
            case DELETE:
                ignores.addAll(customConfig.getIgnores()
                        .getDelete());
                break;
            case OPTIONS:
                ignores.addAll(customConfig.getIgnores()
                        .getOptions());
                break;
            default:
                break;
        }

        ignores.addAll(customConfig.getIgnores().getPattern());

        if (!ignores.isEmpty()) {
            for (String ignore : ignores) {
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(ignore, method);
                if (matcher.matches(request)) {
                    return true;
                }
            }
        }
        return false;
    }
}
