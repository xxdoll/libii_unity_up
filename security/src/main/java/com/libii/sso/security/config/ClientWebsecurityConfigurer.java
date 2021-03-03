package com.libii.sso.security.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableOAuth2Sso
public class ClientWebsecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("urlFilterInvocationSecurityMetadataSource")
    UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;

    @Autowired
    @Qualifier("urlAccessDecisionManager")
    AccessDecisionManager urlAccessDecisionManager;

    @Autowired
    @Qualifier("securityAccessDeniedHandler")
    private AccessDeniedHandler securityAccessDeniedHandler;

    @Autowired
    @Qualifier("securityAuthenticationEntryPoint")
    private AuthenticationEntryPoint securityAuthenticationEntryPoint;

    @Value("${auth-server}")
    public String auth_server;

     /**
     * 访问静态资源
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
    public void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .withObjectPostProcessor(urlObjectPostProcessor());

        http
                .exceptionHandling()
                .authenticationEntryPoint(securityAuthenticationEntryPoint)
                .accessDeniedHandler(securityAccessDeniedHandler);

        http.
                logout()
                .logoutSuccessUrl(auth_server + "/logout")
                .deleteCookies("unity_up-session");

        // 不加会导致退出 不支持GET方式
        http.csrf().disable();

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

}
