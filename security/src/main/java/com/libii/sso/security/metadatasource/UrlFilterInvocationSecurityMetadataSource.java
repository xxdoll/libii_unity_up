package com.libii.sso.security.metadatasource;

import com.google.common.collect.Sets;
import com.libii.sso.common.model.Constant;
import com.libii.sso.security.ignore.CustomConfig;
import com.libii.sso.security.util.ResponseUtils;
import com.libii.sso.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
    private CustomConfig customConfig;
    @Autowired
    private KeycloakRestTemplate restTemplate;
    @Value("${rpc.prefix.sso}")
    public String sso;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        HttpServletRequest request = ((FilterInvocation) o).getHttpRequest();
        HttpServletResponse response = ((FilterInvocation) o).getHttpResponse();
        // 添加来自整合页面的跨域请求头
        ResponseUtils.addResponseHeader(request, response, null);

        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        if (requestUrl.contains("?")) {
            requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
        }
        String requesMethod =  ((FilterInvocation) o).getHttpRequest().getMethod().toUpperCase();
        // 免登陆放行
        if(unloginCheck(request)){
            return null;
        }
        // 检查是否为放行的请求,免鉴权
        if (checkIgnores(request)){
            return SecurityConfig.createList("ROLE_ORIGIN");
        }
        if("OPTIONS".equals(requesMethod)) {
            return null;
        }
        // 获取当前用户的 accesstoken(首次访问无token信息)，从sso系统获取用户角色信息
        KeycloakSecurityContext context = SecurityUtils.getKeycloakSecurityContext();
        if(null == context){
            return SecurityConfig.createList("ROLE_LOGIN");
        }
        // HTTP请求到SSO系统获取 Role 和对应访问路径信息
        String authRole = sso + Constant.AUTH_MENU;
        ParameterizedTypeReference<List<Map<String,String[]>>> responseType = new ParameterizedTypeReference <List<Map<String,String[]>>>(){};
        ResponseEntity<List<Map<String,String[]>>> exchange = restTemplate.exchange(authRole,
                HttpMethod.POST,
                new HttpEntity<>(null, new HttpHeaders()),   //加入headers
                responseType);
        List<Map<String,String[]>> menuMap = exchange.getBody();
        //key--modouleId:{resource url}：method    value--角色信息
        for (Map<String, String[]> map : menuMap) {
            for (String url : map.keySet()) {
                String[] split = url.split(":");
                //匹配当前系统资源 id ,本系统资源路径做进一步校验
                if (!Objects.equals(split[0], moduleId)){
                    continue;
                }
                return SecurityConfig.createList(map.get(url));
                //基于 url 粒度级的权限拦截（当前系统只以系统id进行权限控制）
//                AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(split[1], split[2]);
//                //如果有资源访问权限，角色信息加入security
//                if(antPathMatcher.matches(request)){
//                    return SecurityConfig.createList(map.get(url));
//                }
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
    /**
     * 登陆放行接口
     */
    private boolean unloginCheck(HttpServletRequest request){
        String method = request.getMethod();

        HttpMethod httpMethod = HttpMethod.resolve(method);
        if (null == httpMethod) {
            httpMethod = HttpMethod.GET;
        }

        Set<String> unlogins = Sets.newHashSet();

        switch (httpMethod) {
            case GET:
                unlogins.addAll(customConfig.getUnlogin()
                        .getGet());
                break;
            case PUT:
                unlogins.addAll(customConfig.getUnlogin()
                        .getPut());
                break;
            case HEAD:
                unlogins.addAll(customConfig.getUnlogin()
                        .getHead());
                break;
            case POST:
                unlogins.addAll(customConfig.getUnlogin()
                        .getPost());
                break;
            case PATCH:
                unlogins.addAll(customConfig.getUnlogin()
                        .getPatch());
                break;
            case TRACE:
                unlogins.addAll(customConfig.getUnlogin()
                        .getTrace());
                break;
            case DELETE:
                unlogins.addAll(customConfig.getUnlogin()
                        .getDelete());
                break;
            case OPTIONS:
                unlogins.addAll(customConfig.getUnlogin()
                        .getOptions());
                break;
            default:
                break;
        }

        unlogins.addAll(customConfig.getUnlogin().getPattern());

        if (!unlogins.isEmpty()) {
            for (String unlogin : unlogins) {
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(unlogin, method);
                if (matcher.matches(request)) {
                    return true;
                }
            }
        }
        return false;

    }
}
