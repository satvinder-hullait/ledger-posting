package com.zing.ledger.adapter.command;

public class LedgerWriteRepositoryException extends RuntimeException {

    public LedgerWriteRepositoryException(String message, Exception e) {
        super(message, e);
    }
}
