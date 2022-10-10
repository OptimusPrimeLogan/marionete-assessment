package com.marionete.assessment.controller;

import com.marionete.assessment.model.UserCredential;
import com.marionete.assessment.service.MarioneteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/marionete")
@Slf4j
public class MarioneteController {

    @Autowired
    private MarioneteService marioneteService;

    @PostMapping(value = "/useraccount",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserAccountDetails(@RequestBody UserCredential userCredential) {
        return new ResponseEntity<>(marioneteService.getUserAccountDetails(userCredential), HttpStatus.OK);
    }

}
