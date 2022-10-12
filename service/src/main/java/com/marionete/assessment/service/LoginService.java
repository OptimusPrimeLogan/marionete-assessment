package com.marionete.assessment.service;

import com.marionete.assessment.exception.TokenException;
import com.marionete.assessment.model.UserCredential;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import services.LoginRequest;
import services.LoginResponse;
import services.LoginServiceGrpc;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginService extends LoginServiceGrpc.LoginServiceImplBase {

    private Server grpcServer;

    @Value("${grpc.service.host}")
    protected String grpcServiceHost;

    @Value("${grpc.service.port}")
    protected Integer grpcServicePort;

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        LoginResponse response = LoginResponse.newBuilder().setToken(UUID.randomUUID().toString())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public LoginResponse loginWithCred(LoginRequest loginRequest){
        LoginServiceGrpc.LoginServiceBlockingStub blockingStub = LoginServiceGrpc.newBlockingStub(
                ManagedChannelBuilder.forAddress(grpcServiceHost, grpcServicePort).usePlaintext().build());
        return blockingStub.login(loginRequest);
    }

    public void startServer() {
        try {
            grpcServer = ServerBuilder.forPort(grpcServicePort).addService(new LoginService()).build();
            if(grpcServer.isShutdown()){
                grpcServer.start();
            }

            log.info("Login GRPC server is listening on port {}", grpcServicePort);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (grpcServer != null) {
                    try {
                        grpcServer.shutdown().awaitTermination(10, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getToken(UserCredential userCredential){

        String token = "";

        try{
            LoginRequest loginRequest = LoginRequest.newBuilder()
                    .setUsername(userCredential.getUsername())
                    .setPassword(userCredential.getPassword()).build();

            LoginResponse response = loginWithCred(loginRequest);
            token = response.getToken();

        }catch (TokenException te){
            te.printStackTrace();
            log.error("Error generating token "+te.getMessage());
            throw new TokenException(te.getMessage());
        }

        return token;
    }

}
