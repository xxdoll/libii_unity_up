package com.libii.sso.security.config;

import com.libii.sso.security.handler.KeyCloakAuthSuccessHandler;
import com.libii.sso.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;


@EnableGlobalMethodSecurity(prePostEnabled=true)
@KeycloakConfiguration
public class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    /**
     * 自定义授权访问策略
     */
    @Autowired
    @Qualifier("urlAccessDecisionManager")
    AccessDecisionManager urlAccessDecisionManager;
    /**
     * Token授权处理
     */
    @Autowired
    @Qualifier("securityAuthenticationProvider")
    private AuthenticationProvider securityAuthenticationProvider;
    /**
     * 自定义权限拦截控制器
     */
    @Autowired
    @Qualifier("urlFilterInvocationSecurityMetadataSource")
    private UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;
    /**
     * 捕获验证失败异常处理
     */
    @Autowired
    @Qualifier("securityAccessDeniedHandler")
    private AccessDeniedHandler securityAccessDeniedHandler;

    /**
     * 开启 SpringBoot yml 配置，不依赖keycloak.json文件
     * @return
     */
    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(securityAuthenticationProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    //=========================双重检验=================================
    @Bean
    public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
            KeycloakAuthenticationProcessingFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(true);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
            KeycloakPreAuthActionsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(true);
        return registrationBean;
    }
    //=========================双重检验=================================

    /**
     * 会话身份验证策略
     * @return
     */
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    /**
     * 放行资源
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/css/**",
                "/js/**",
                "/images/**",
                "/fonts/**",
                "/favicon.ico",
                "/static/**",
                "/resources/**",
                "/error");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .withObjectPostProcessor(urlObjectPostProcessor());
        http
                .exceptionHandling()
                .accessDeniedHandler(securityAccessDeniedHandler)
                .and()
                .logout()
                .addLogoutHandler(keycloakLogoutHandler())
                .logoutUrl("/sso/logout")
                .logoutSuccessUrl("/platforms")
                .permitAll();
        http
                .csrf().disable();

    }

    public ObjectPostProcessor urlObjectPostProcessor() {
        return new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                o.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource);
                o.setAccessDecisionManager(urlAccessDecisionManager);
                return o;
            }
        };
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    @Override
    protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
        KeycloakAuthenticationProcessingFilter filter = new KeycloakAuthenticationProcessingFilter(authenticationManagerBean());
        filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
        filter.setAuthenticationSuccessHandler(keyCloakAuthSuccessHandler());
        return filter;
    }


    @Bean
    public KeyCloakAuthSuccessHandler keyCloakAuthSuccessHandler() {
        return new KeyCloakAuthSuccessHandler(new SavedRequestAwareAuthenticationSuccessHandler());
    }
}
