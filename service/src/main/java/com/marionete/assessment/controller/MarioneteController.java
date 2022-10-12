package com.marionete.assessment.controller;

import com.marionete.assessment.model.UserAccount;
import com.marionete.assessment.model.UserCredential;
import com.marionete.assessment.service.MarioneteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/marionete")
@Slf4j
public class MarioneteController {

    @Autowired
    private MarioneteService marioneteService;

    @PostMapping(value = "/useraccount",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<UserAccount>> getUserAccountDetails(@Valid @RequestBody UserCredential userCredential) {
        return new ResponseEntity<>(marioneteService.getUserAccountDetails(userCredential), HttpStatus.OK);
    }

}
