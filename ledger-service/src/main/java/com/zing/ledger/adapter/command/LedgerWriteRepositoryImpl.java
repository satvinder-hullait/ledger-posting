package com.zing.ledger.adapter.command;

import com.zing.ledger.service.domain.TransactionMessage;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@AllArgsConstructor
public class LedgerWriteRepositoryImpl implements LedgerWriteRepository {

    private final RedisTemplate<String, ArrayList<TransactionMessage>> redisTemplate;

    @Override
    public void saveTransaction(TransactionMessage transactionMessage) {
        String key = transactionMessage.accountId();
        try {
            ArrayList<TransactionMessage> existingMessages = redisTemplate.opsForValue().get(key);
            if (existingMessages == null) {
                existingMessages = new ArrayList<>();
            }
            if (existingMessages.contains(transactionMessage)) {
                throw new TransactionAlreadyExistsException(
                        "Duplicate transaction for account: " + key);
            }
            existingMessages.add(transactionMessage);
            log.info("Writing transaction");
            redisTemplate.opsForValue().set(key, existingMessages);
        } catch (TransactionAlreadyExistsException e) {
            //         Want this exception to be thrown and not go into the catch all below
            throw e;
        } catch (Exception e) {
            throw new LedgerWriteRepositoryException(
                    "Failed to save transaction for account: " + key, e);
        }
    }
}
