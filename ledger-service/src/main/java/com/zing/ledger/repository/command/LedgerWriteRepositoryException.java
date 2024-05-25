package com.zing.ledger.repository.command;

public class LedgerWriteRepositoryException extends RuntimeException {

    public LedgerWriteRepositoryException(String message, Exception e) {
        super(message, e);
    }
}
