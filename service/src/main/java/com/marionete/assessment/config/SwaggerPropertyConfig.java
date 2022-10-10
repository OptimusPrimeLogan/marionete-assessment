package com.marionete.assessment.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
/**
 * Property Loader for Swagger
 */
@Configuration
@EnableConfigurationProperties({
        SwaggerProperties.class
})
public class SwaggerPropertyConfig {}
