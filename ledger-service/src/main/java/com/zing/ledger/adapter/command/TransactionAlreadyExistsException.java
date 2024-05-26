package com.zing.ledger.adapter.command;

public class TransactionAlreadyExistsException extends RuntimeException {

    public TransactionAlreadyExistsException(String message) {
        super(message);
    }
}
