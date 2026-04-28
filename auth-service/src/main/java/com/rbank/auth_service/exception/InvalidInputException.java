package com.rbank.auth_service.exception;

public class InvalidInputException extends  IllegalArgumentException{
    public InvalidInputException(String message){
        super(message);
    }
}
