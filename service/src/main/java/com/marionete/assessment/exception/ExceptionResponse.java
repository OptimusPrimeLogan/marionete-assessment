package com.marionete.assessment.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class ExceptionResponse {
    private String status;
    private String message;
}