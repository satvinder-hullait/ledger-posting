package com.zing.ledger.service;

import com.zing.ledger.adapter.command.LedgerWriteRepository;
import com.zing.ledger.adapter.query.LedgerQueryRepository;
import com.zing.ledger.service.domain.AccountType;
import com.zing.ledger.service.domain.TransactionCurrency;
import com.zing.ledger.service.domain.TransactionMessage;
import com.zing.ledger.service.domain.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LedgerServiceImplTest {

    @Mock private LedgerWriteRepository ledgerWriteRepository;
    @Mock private LedgerQueryRepository ledgerQueryRepository;

    @Test
    void testWriteLedgerEntry() {
        String accountId = "1234";
        String transactionId = "12345";
        TransactionMessage transactionMessage =
                new TransactionMessage(
                        transactionId,
                        accountId,
                        AccountType.CURRENT,
                        BigDecimal.ONE,
                        TransactionType.CREDIT,
                        TransactionCurrency.GBP,
                        Instant.now());
        LedgerService ledgerService =
                new LedgerServiceImpl(ledgerWriteRepository, ledgerQueryRepository);

        ledgerService.writeLedgerEntry(transactionMessage);

        Mockito.verify(ledgerWriteRepository, Mockito.times(1)).saveTransaction(transactionMessage);
    }
}
