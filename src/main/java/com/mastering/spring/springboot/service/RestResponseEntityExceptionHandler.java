package com.mastering.spring.springboot.service;

import com.mastering.spring.springboot.bean.ExceptionResponse;
import com.mastering.spring.springboot.bean.TodoNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler /*extends ResponseEntityExceptionHandler*/ {
    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<ExceptionResponse>
    todoNotFound(TodoNotFoundException ex) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse( ex.getMessage(),
                        "Any details you would want to add");
        return new ResponseEntity<>
                (exceptionResponse, new HttpHeaders(),
                        HttpStatus.NOT_FOUND);
    }
}
