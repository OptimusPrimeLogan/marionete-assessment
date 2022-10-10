package com.marionete.assessment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties Placeholder for Swagger
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.app.swagger")
public class SwaggerProperties {
    private String version;
    private String title;
    private String description;
    private String termsOfServiceUrl;
    private String license;
    private String licenseUrl;
    private String contactName;
    private String contactUrl;
    private String contactEmail;
}
