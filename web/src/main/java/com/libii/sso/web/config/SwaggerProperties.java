package com.libii.sso.web.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("swagger")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwaggerProperties {

	private String title;
	private String basepackage;
	private String version;
	private String contractName;
	private String description;
	private String contractUrl;
	private String contractEmail;
}
