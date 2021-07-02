package com.yellowpeper.fundstransferapp.exception;

public class BalanceOriginNegativeException extends RuntimeException {

    public BalanceOriginNegativeException(String message) {
        super(message);
    }
}
