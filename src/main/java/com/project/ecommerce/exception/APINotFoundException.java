package com.project.ecommerce.exception;

public class APINotFoundException extends RuntimeException {
    String message;
    public APINotFoundException(String message){
        super(message);
    }
    public APINotFoundException(){

    }
}
