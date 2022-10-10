package com.marionete.assessment.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {
    private String status;
    private String message;
}