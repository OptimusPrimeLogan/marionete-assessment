package com.marionete.assessment.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountServiceTest {

    @InjectMocks
    AccountService accountService;

    @Test
    void shouldGetAccountDetails(){
        final var mock = mock(WebClient.class);
        final var uriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        final var headersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        final var responseSpecMock = mock(ResponseSpec.class);

        lenient().when(mock.get()).thenReturn(uriSpecMock);
        lenient().when(uriSpecMock.uri(ArgumentMatchers.<String>notNull())).thenReturn(headersSpecMock);
        lenient().when(headersSpecMock.header(notNull(), notNull())).thenReturn(headersSpecMock);
        lenient().when(headersSpecMock.headers(notNull())).thenReturn(headersSpecMock);
        lenient().when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        lenient().when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<String>>notNull()))
                .thenReturn(Mono.empty());

        Assertions.assertNotNull(accountService.getAccountDetails("Test"));
    }

}
