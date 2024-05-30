package com.marionete.assessment.config;

import com.marionete.assessment.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class gRPCConfig {

    @Autowired
    private LoginService loginService;

    @Bean
    public void loginServer() {
        loginService.startServer();
    }
}
