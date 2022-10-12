package com.marionete.assessment.service;

import com.marionete.assessment.model.UserAccount;
import com.marionete.assessment.model.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MarioneteService {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    public Mono<UserAccount> getUserAccountDetails(UserCredential userCredential){
        String token = loginService.getToken(userCredential);

        return userService.getUserDetails(token)
                .zipWith(accountService.getAccountDetails(token),
                        (user, account) -> UserAccount.builder().accountInfo(account).userInfo(user).build());
    }
}
