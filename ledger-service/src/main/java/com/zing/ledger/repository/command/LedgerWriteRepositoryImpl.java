package com.zing.ledger.repository.command;

import com.zing.ledger.service.domain.TransactionMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@AllArgsConstructor
public class LedgerWriteRepositoryImpl implements LedgerWriteRepository {

    private static final int ZERO_BALANCE = 0;

    //        public void addFunds(TransactionMessage transaction) {
    //            final var accountId = transaction.accountId();
    //            log.info("Request to add transaction for '{}'", accountId);
    //            if (ledgerDatastore.containsKey(accountId)) {
    //                log.info("Adding '{}' of '{}' to wallet: '{}'", transaction.transactionType(),
    // transaction.amount(), accountId);
    //                ledger.get(accountId)
    //                    .add(new TransactionMessage(
    //                        transaction.accountId(),
    //                        transaction.accountType(),
    //                        transaction.amount(),
    //                        transaction.transactionType(),
    //                        transaction.timestamp()));
    //            } else {
    //                var treeSet = new TreeSet<>(comparing(TransactionMessage::timestamp));
    //                treeSet.add(new TransactionMessage(
    //                    transaction.accountId(),
    //                    transaction.accountType(),
    //                    transaction.amount(),
    //                    transaction.transactionType(),
    //                    transaction.timestamp()));
    //                ledger.put(accountId, treeSet);
    //            }
    ////            return new WalletResponse(walletId, ledger.get(walletId).last().balance());
    //        }

    @Override
    public void saveTransaction(TransactionMessage transactionMessage) {}
}
