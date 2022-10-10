package com.marionete.assessment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ExceptionResponse handleGenericException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        ExceptionResponse error = new ExceptionResponse();
        error.setMessage(ex.getMessage());
        error.setStatus("Invalid Input");
        return error;
    }

    @ExceptionHandler(TokenException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public @ResponseBody ExceptionResponse handleTokenException(TokenException ex, WebRequest request) {
        ex.printStackTrace();
        ExceptionResponse error = new ExceptionResponse();
        error.setMessage(ex.getMessage());
        error.setStatus("Invalid Input");
        return error;
    }

}