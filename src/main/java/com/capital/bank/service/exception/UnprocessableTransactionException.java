package com.capital.bank.service.exception;

public class UnprocessableTransactionException extends RuntimeException {
    public UnprocessableTransactionException(String message) {
        super(message);
    }
}
