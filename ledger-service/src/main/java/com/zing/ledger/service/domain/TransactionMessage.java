package com.zing.ledger.service.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public record TransactionMessage(
        String transactionId,
        String accountId,
        AccountType accountType,
        BigDecimal amount,
        TransactionType transactionType,
        TransactionCurrency transactionCurrency,
        Instant timestamp)
        implements Serializable {}
