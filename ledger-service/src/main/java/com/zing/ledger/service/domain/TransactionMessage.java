package com.zing.ledger.service.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionMessage(
        String accountId,
        AccountType accountType,
        BigDecimal amount,
        TransactionType transactionType,
        Instant timestamp) {}
