package com.marionete.assessment.service;

import com.marionete.assessment.model.Account;
import com.marionete.assessment.model.User;
import com.marionete.assessment.model.UserAccount;
import com.marionete.assessment.model.UserCredential;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MarioneteServiceTest {

    @InjectMocks
    MarioneteService marioneteService;

    @Mock
    AccountService accountService;

    @Mock
    UserService userService;

    @Mock
    LoginService loginService;

    Mono<User> userMono = Mono.just(new User());
    Mono<Account> accMono = Mono.just(new Account());
    Mono<UserAccount> uaMono = Mono.just(UserAccount.builder().accountInfo(new Account()).userInfo(new User()).build());

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetUserAccountDetails(){

        lenient().when(loginService.getToken(Mockito.any(UserCredential.class))).thenReturn("token");
        lenient().when(userService.getUserDetails(null)).thenReturn(userMono);
        lenient().when(accountService.getAccountDetails(null)).thenReturn(accMono);

        Assertions.assertNotNull(marioneteService.getUserAccountDetails(Mockito.any(UserCredential.class)));

    }
}
