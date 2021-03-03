package com.libii.sso.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@ConfigurationProperties(prefix = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationConfig implements Serializable {
    private String[] origins;
}
