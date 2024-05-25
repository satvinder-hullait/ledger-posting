package com.zing.ledger.repository.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class LedgerWriteRepositoryImplTest extends BaseRedisIntegrationTestClass {

    @Autowired private LedgerWriteRepositoryImpl ledgerWriteRepository;

    @Autowired private RedisTemplate<String, ArrayList<TransactionMessage>> redisTemplate;

    @Test
    void testSaveTransaction_NewAccount() {
        String accountId = "1234";
        TransactionMessage newMessage =
                new TransactionMessage(
                        accountId,
                        AccountType.CURRENT,
                        BigDecimal.ONE,
                        TransactionType.CREDIT,
                        TransactionCurrency.GBP,
                        Instant.now());

        ledgerWriteRepository.saveTransaction(newMessage);

        ArrayList<TransactionMessage> retrievedMessages =
                redisTemplate.opsForValue().get(accountId);
        assertThat(retrievedMessages).isNotNull();
        assertThat(retrievedMessages.size()).isEqualTo(1);

        TransactionMessage retrievedMessage = retrievedMessages.getFirst();
        assertThat(retrievedMessage.accountId()).isEqualTo(newMessage.accountId());
        assertThat(retrievedMessage.accountType()).isEqualTo(newMessage.accountType());
        assertThat(retrievedMessage.amount()).isEqualTo(newMessage.amount());
        assertThat(retrievedMessage.transactionType()).isEqualTo(newMessage.transactionType());
        assertThat(retrievedMessage.timestamp()).isEqualTo(newMessage.timestamp());
    }

    @Test
    void testSaveTransaction_ExistingAccount() {
        String accountId = "1234";
        TransactionMessage existingMessage =
                new TransactionMessage(
                        accountId,
                        AccountType.CURRENT,
                        BigDecimal.ONE,
                        TransactionType.CREDIT,
                        TransactionCurrency.GBP,
                        Instant.now());
        TransactionMessage newMessage =
                new TransactionMessage(
                        accountId,
                        AccountType.CURRENT,
                        BigDecimal.TEN,
                        TransactionType.DEBIT,
                        TransactionCurrency.GBP,
                        Instant.now());

        redisTemplate.opsForValue().set(accountId, new ArrayList<>(List.of(existingMessage)));
        ledgerWriteRepository.saveTransaction(newMessage);

        ArrayList<TransactionMessage> retrievedMessages =
                redisTemplate.opsForValue().get(accountId);
        assertThat(retrievedMessages).isNotNull();
        assertThat(retrievedMessages.size()).isEqualTo(2);

        TransactionMessage firstMessage = retrievedMessages.get(0);
        TransactionMessage secondMessage = retrievedMessages.get(1);

        assertThat(firstMessage.accountId()).isEqualTo(existingMessage.accountId());
        assertThat(firstMessage.accountType()).isEqualTo(existingMessage.accountType());
        assertThat(firstMessage.amount()).isEqualTo(existingMessage.amount());
        assertThat(firstMessage.transactionType()).isEqualTo(existingMessage.transactionType());
        assertThat(firstMessage.timestamp()).isEqualTo(existingMessage.timestamp());

        assertThat(secondMessage.accountId()).isEqualTo(newMessage.accountId());
        assertThat(secondMessage.accountType()).isEqualTo(newMessage.accountType());
        assertThat(secondMessage.amount()).isEqualTo(newMessage.amount());
        assertThat(secondMessage.transactionType()).isEqualTo(newMessage.transactionType());
        assertThat(secondMessage.timestamp()).isEqualTo(newMessage.timestamp());
    }

    @Test
    // Suppress "Unchecked generics array creation for varargs parameter" on
    // redisTemplate.opsForSet().add(accountId, messageList);
    @SuppressWarnings("unchecked")
    void testSaveTransaction_ThrowsException() {
        String accountId = "invalidKey";
        TransactionMessage newMessage =
                new TransactionMessage(
                        accountId,
                        AccountType.CURRENT,
                        BigDecimal.ONE,
                        TransactionType.CREDIT,
                        TransactionCurrency.GBP,
                        Instant.now());
        ArrayList<TransactionMessage> messageList = new ArrayList<>();
        messageList.add(newMessage);
        redisTemplate.opsForSet().add(accountId, messageList);

        assertThatThrownBy(() -> ledgerWriteRepository.saveTransaction(newMessage))
                .isInstanceOf(LedgerWriteRepositoryException.class)
                .hasMessageContaining("Failed to save transaction for account: " + accountId);
    }
}
