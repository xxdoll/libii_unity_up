package com.libii.sso;

import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.libii.sso.**.dao")
@EnableScheduling
@Configuration
@EnableCaching // 缓存
@EnableSwagger2
@EnableDiscoveryClient
public class WebApplication extends SpringBootServletInitializer {

    @Override
    public SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebApplication.class);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced
    public KeycloakRestTemplate keycloakRestTemplate(){
        return  new KeycloakRestTemplate(new KeycloakClientRequestFactory());
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}

