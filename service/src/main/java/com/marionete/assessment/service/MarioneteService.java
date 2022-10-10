package com.marionete.assessment.service;

import com.marionete.assessment.exception.TokenException;
import com.marionete.assessment.model.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import services.LoginRequest;
import services.LoginResponse;
import services.LoginServiceGrpc;

@Service
@Slf4j
public class MarioneteService {

    private LoginServiceGrpc.LoginServiceBlockingStub blockingStub;
    public String getUserAccountDetails(UserCredential userCredential){
        String token = "";

        try{
            LoginRequest loginRequest = LoginRequest.newBuilder()
                    .setUsername(userCredential.getUsername())
                    .setPassword(userCredential.getPassword()).build();

            LoginResponse response = blockingStub.login(loginRequest);
            token = response.getToken();

        }catch (TokenException te){
            te.printStackTrace();
            log.error("Error generating token "+te.getMessage());
            throw new TokenException(te.getMessage());
        }

        log.info(token);

        return token;

    }
}
