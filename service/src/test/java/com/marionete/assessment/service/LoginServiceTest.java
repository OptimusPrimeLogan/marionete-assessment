package com.marionete.assessment.service;

import com.marionete.assessment.model.UserCredential;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginServiceTest {

    @InjectMocks
    LoginService loginService;

    @Test
    void shouldGetToken(){
        Assertions.assertThrows(Exception.class, () -> loginService.getToken(new UserCredential()));
    }


}
