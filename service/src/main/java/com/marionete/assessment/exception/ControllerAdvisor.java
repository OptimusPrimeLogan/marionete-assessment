package com.marionete.assessment.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    private final String INVALID_INPUT = "Invalid Input";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ExceptionResponse handleGenericException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        ExceptionResponse error = new ExceptionResponse();
        error.setMessage(ex.getMessage());
        error.setStatus(INVALID_INPUT);
        return error;
    }

    @ExceptionHandler(TokenException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public @ResponseBody ExceptionResponse handleTokenException(TokenException ex, WebRequest request) {
        ex.printStackTrace();
        ExceptionResponse error = new ExceptionResponse();
        error.setMessage(ex.getMessage());
        error.setStatus(INVALID_INPUT);
        return error;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.add(error.getDefaultMessage());
        });
        ExceptionResponse error = new ExceptionResponse();
        error.setMessage(errors.toString());
        error.setStatus(INVALID_INPUT);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}