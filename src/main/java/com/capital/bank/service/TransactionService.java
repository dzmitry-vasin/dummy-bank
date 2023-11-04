package com.capital.bank.service;

import com.capital.bank.model.Account;
import com.capital.bank.model.Transaction;
import com.capital.bank.repository.AccountRepository;
import com.capital.bank.repository.TransactionRepository;
import com.capital.bank.service.exception.UnprocessableTransactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public List<Transaction> findAll(Long accountExternalId, int offset, int limit) {
        return transactionRepository.
                findAllByFromAccountExternalIdOrToAccountExternalIdOrderByTimestampDesc(
                        accountExternalId,
                        accountExternalId,
                        PageRequest.of(offset, limit)
                );
    }

    @Transactional
    public void save(Transaction transaction) {
        verifyAccountsDifference(transaction);

        Account fromAccount = accountRepository.findByExternalId(
                transaction.getFromAccount().getExternalId()
        );

        Account toAccount = accountRepository.findByExternalId(
                transaction.getToAccount().getExternalId()
        );

        verifyFromAccountBalance(transaction, fromAccount);

        fromAccount.setBalance(
                fromAccount.getBalance()
                        .subtract(
                                transaction.getAmount()
                        )
        );
        toAccount.setBalance(
                toAccount.getBalance()
                        .add(
                                transaction.getAmount()
                        )
        );

        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);

        transactionRepository.save(transaction);
    }

    private void verifyFromAccountBalance(Transaction transaction, Account fromAccount) {
        if (fromAccount.getBalance().subtract(transaction.getAmount()).compareTo(BigDecimal.ZERO) < 0)
            throw new UnprocessableTransactionException(
                    String.format(
                            "Not enough money to transfer from %s account, current balance is %s.",
                            fromAccount.getExternalId(),
                            fromAccount.getBalance()
                    )
            );
    }

    private void verifyAccountsDifference(Transaction transaction) {
        if (transaction.getFromAccount().getExternalId().equals(transaction.getToAccount().getExternalId()))
            throw new UnprocessableTransactionException(
                    String.format(
                            "Can't execute the transfer operation within the same %s account.",
                            transaction.getFromAccount().getExternalId()
                    )
            );
    }
}
