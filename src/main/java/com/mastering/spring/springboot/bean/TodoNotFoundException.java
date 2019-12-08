package com.mastering.spring.springboot.bean;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(String message) {
        super(message);
    }
}
