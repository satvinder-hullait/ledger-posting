package com.zing.ledger.adapter.query;

import com.zing.ledger.service.domain.TransactionMessage;
import java.time.Instant;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Slf4j
public class LedgerQueryRepositoryImpl implements LedgerQueryRepository {

    private final RedisTemplate<String, ArrayList<TransactionMessage>> redisTemplate;

    @Override
    public Page<TransactionMessage> getTransactionsForAccount(String accountId, Instant timestamp) {
        ArrayList<TransactionMessage> result = redisTemplate.opsForValue().get(accountId);
        if (result == null) {
            throw new LedgerQueryRepositoryException(
                    String.format("Account: %s, does not exist in ledger", accountId));
        }

        Page<TransactionMessage> page = new PageImpl<>(new ArrayList<>(result));
        log.info("Getting transactions '{}'", page.stream().count());

        return page;
    }
}
