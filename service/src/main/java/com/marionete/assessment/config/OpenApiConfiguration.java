package com.marionete.assessment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration for Open-API Swagger
 */
@Configuration
@Profile({"local", "dev", "test"})//To enable per environment
public class OpenApiConfiguration {

    @Bean
    public OpenAPI api(SwaggerProperties swaggerProperty) {
        return new OpenAPI().info(getInfo(swaggerProperty));
    }

    /**
     * To setup info
     * @param swaggerProperty
     * @return
     */
    private Info getInfo(SwaggerProperties swaggerProperty) {
        return new Info()
                .contact(getContact(swaggerProperty))
                .title(swaggerProperty.getTitle())
                .description(swaggerProperty.getDescription())
                .version(swaggerProperty.getVersion());
    }

    /**
     * To setup contact details
     * @param swaggerProperty
     * @return
     */
    private Contact getContact(SwaggerProperties swaggerProperty) {
        Contact contact = new Contact();
        contact.setEmail(swaggerProperty.getContactEmail());
        contact.setName(swaggerProperty.getContactName());
        contact.setUrl(swaggerProperty.getContactUrl());
        return contact;
    }

}