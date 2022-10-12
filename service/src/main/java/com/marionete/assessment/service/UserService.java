package com.marionete.assessment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marionete.assessment.model.Account;
import com.marionete.assessment.model.User;
import com.marionete.backends.UserInfoMock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@Slf4j
public class UserService {

    static {
        UserInfoMock.start();
    }

    @Value("${user.details.base.url}")
    private String userDetailsBaseUrl;

    @Value("${user.details.uri}")
    private String detailsUri;

    public Mono<User> getUserDetails(String authToken){

        return WebClient.builder().baseUrl(userDetailsBaseUrl).build().get()
                .uri(detailsUri)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
                .retrieve()
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals,
                        response -> Mono.error(new ResponseStatusException(response.statusCode())))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new ResponseStatusException(response.statusCode())))
                .bodyToMono(String.class)
                .map(s -> {
                    try {
                        return new ObjectMapper().readValue(s, User.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                .filter(throwable -> throwable instanceof ResponseStatusException));

    }

}
