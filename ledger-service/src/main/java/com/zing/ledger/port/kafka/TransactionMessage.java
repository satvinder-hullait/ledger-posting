package com.zing.ledger.port.kafka;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionMessage(String accountId, AccountType accountType, BigDecimal amount, TransactionType transactionType, Instant timestamp) {
}
