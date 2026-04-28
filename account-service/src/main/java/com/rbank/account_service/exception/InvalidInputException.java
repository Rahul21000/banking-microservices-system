package com.rbank.account_service.exception;

public class InvalidInputException extends IllegalArgumentException{
    public InvalidInputException(String message){
        super(message);
    }
}
