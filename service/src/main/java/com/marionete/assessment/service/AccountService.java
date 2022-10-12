package com.marionete.assessment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marionete.assessment.model.Account;
import com.marionete.assessment.model.User;
import com.marionete.backends.AccountInfoMock;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
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
public class AccountService {

    static {
        AccountInfoMock.start();
    }

    @Value("${account.details.base.url}")
    private String accountDetailsBaseUrl;

    @Value("${account.details.uri}")
    private String detailsUri;

    public Mono<Account> getAccountDetails(String authToken){

        return  WebClient.builder().baseUrl(accountDetailsBaseUrl).build()
                .get()
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
                        return new ObjectMapper().readValue(s, Account.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof ResponseStatusException));

    }

}
