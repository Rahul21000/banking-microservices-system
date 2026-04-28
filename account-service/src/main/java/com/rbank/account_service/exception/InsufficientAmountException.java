package com.rbank.account_service.exception;

public class InsufficientAmountException extends RuntimeException{
    public InsufficientAmountException(String message){
        super(message);
    }
}
