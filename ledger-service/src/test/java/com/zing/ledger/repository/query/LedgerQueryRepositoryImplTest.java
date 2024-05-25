package com.zing.ledger.repository.query;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.zing.ledger.BaseRedisIntegrationTestClass;
import com.zing.ledger.service.domain.AccountType;
import com.zing.ledger.service.domain.TransactionCurrency;
import com.zing.ledger.service.domain.TransactionMessage;
import com.zing.ledger.service.domain.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class LedgerQueryRepositoryImplTest extends BaseRedisIntegrationTestClass {

    @Autowired private LedgerQueryRepository ledgerQueryRepository;

    @Autowired private RedisTemplate<String, ArrayList<TransactionMessage>> redisTemplate;

    @Test
    void testGetTransactionsForNonExistingAccount() {
        assertThatThrownBy(
                        () -> ledgerQueryRepository.getTransactionsForAccount("nonExistingAccount"))
                .isInstanceOf(LedgerQueryRepositoryException.class);
    }

    @Test
    void testCanGetTransactions() {
        String accountId = "1234";
        TransactionMessage expectedMessage =
                new TransactionMessage(
                        "1",
                        accountId,
                        AccountType.CURRENT,
                        BigDecimal.ONE,
                        TransactionType.CREDIT,
                        TransactionCurrency.GBP,
                        Instant.now());
        List<TransactionMessage> transactionMessages = List.of(expectedMessage);

        redisTemplate.opsForValue().set(accountId, new ArrayList<>(transactionMessages));

        Page<TransactionMessage> retrievedMessages =
                ledgerQueryRepository.getTransactionsForAccount(accountId);
        assertThat(retrievedMessages.getTotalElements()).isEqualTo(1);

        var actualMessage = retrievedMessages.getContent().getFirst();

        assertThat(actualMessage.accountId()).isEqualTo(expectedMessage.accountId());
        assertThat(actualMessage.transactionId()).isEqualTo(expectedMessage.transactionId());
        assertThat(actualMessage.accountType()).isEqualTo(expectedMessage.accountType());
        assertThat(actualMessage.amount()).isEqualTo(expectedMessage.amount());
        assertThat(actualMessage.transactionType()).isEqualTo(expectedMessage.transactionType());
        assertThat(actualMessage.timestamp()).isEqualTo(expectedMessage.timestamp());
    }
}
