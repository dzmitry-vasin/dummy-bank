package com.capital.bank;

import com.capital.bank.model.Account;
import com.capital.bank.model.Client;
import com.capital.bank.model.Transaction;
import com.capital.bank.repository.AccountRepository;
import com.capital.bank.repository.ClientRepository;
import com.capital.bank.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class TestData {
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public List<Account> prepareAccounts(String clientExternalId) {
        Client anyClient = Client.builder()
                .externalId(clientExternalId)
                .build();

        Account account1 = Account.builder()
                .client(anyClient)
                .externalId(2147483648L)
                .balance(BigDecimal.valueOf(10000))
                .build();

        Account account2 = Account.builder()
                .client(anyClient)
                .externalId(9999999999L)
                .balance(BigDecimal.valueOf(99999))
                .build();

        List<Account> anyAccounts = List.of(account1, account2);

        clientRepository.save(anyClient);
        accountRepository.saveAll(anyAccounts);

        return anyAccounts;
    }

    public List<Transaction> prepareTransactions(int size) {
        List<Account> anyAccounts = prepareAccounts("any");

        List<Transaction> transactions =
                Stream.concat(
                                Stream.generate(
                                                () -> Transaction.builder()
                                                        .toAccount(anyAccounts.get(0))
                                                        .fromAccount(anyAccounts.get(1))
                                                        .amount(BigDecimal.valueOf(10000))
                                                        .build()
                                        )
                                        .limit(size / 2),
                                Stream.generate(
                                                () -> Transaction.builder()
                                                        .toAccount(anyAccounts.get(1))
                                                        .fromAccount(anyAccounts.get(0))
                                                        .amount(BigDecimal.valueOf(10000))
                                                        .build()
                                        )
                                        .limit(size / 2)
                        )
                        .collect(Collectors.toList());

        transactionRepository.saveAll(transactions);

        return transactions;
    }
}
