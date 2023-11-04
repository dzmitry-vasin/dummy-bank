package com.capital.bank.controller;

import com.capital.bank.IntegrationTest;
import com.capital.bank.model.Account;
import com.capital.bank.model.Client;
import com.capital.bank.model.Transaction;
import com.capital.bank.repository.AccountRepository;
import com.capital.bank.repository.ClientRepository;
import com.capital.bank.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AllArgsConstructor
public class TransactionControllerTest {
    private static final Integer DEFAULT_PARAM_VALUE = null;

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @SneakyThrows
    public void givenAccountsWithEqualTo20TransactionsBetween_whenGetTransactions_thenResponseWithAllTransactions() {
        List<Transaction> transactions = prepareTransactions(20);

        verify(
                getTransactionsRequest(DEFAULT_PARAM_VALUE, DEFAULT_PARAM_VALUE),
                transactions
        );
    }

    @Test
    @SneakyThrows
    public void givenAccountsWithMoreThan20TransactionsBetween_whenGetTransactions_thenResponseWith20OnlyTransactions() {
        List<Transaction> transactions = prepareTransactions(30);
        List<Transaction> expectedTransactions = transactions.stream()
                .sorted(
                        Comparator.comparing(Transaction::getTimestamp).reversed()
                )
                .limit(20)
                .toList();

        verify(
                getTransactionsRequest(DEFAULT_PARAM_VALUE, DEFAULT_PARAM_VALUE),
                expectedTransactions
        );
    }

    @Test
    @SneakyThrows
    public void givenAccountsWithMoreThan20AndLessThan40TransactionsBetween_whenGetTransactionsWithOffset_thenResponseWithRemainingTransactions() {
        List<Transaction> transactions = prepareTransactions(30);
        List<Transaction> expectedTransactions = transactions.stream()
                .sorted(
                        Comparator.comparing(Transaction::getTimestamp).reversed()
                )
                .skip(20)
                .toList();

        verify(
                getTransactionsRequest(1, DEFAULT_PARAM_VALUE),
                expectedTransactions
        );

    }

    @Test
    @SneakyThrows
    public void givenAccountsWithLessThan40TransactionsBetween_whenGetTransactionsWithLimit40_thenResponseWithAllTransactions() {
        List<Transaction> transactions = prepareTransactions(30);

        verify(
                getTransactionsRequest(DEFAULT_PARAM_VALUE, 40),
                transactions
        );
    }

    private List<Transaction> prepareTransactions(int size) {
        Client anyClient = Client.builder()
                .externalId("anyClientExternalId")
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

        List<Transaction> transactions =
                Stream.concat(
                                Stream.generate(
                                                () -> Transaction.builder()
                                                        .toAccount(account1)
                                                        .fromAccount(account2)
                                                        .amount(BigDecimal.valueOf(10000))
                                                        .build()
                                        )
                                        .limit(size / 2),
                                Stream.generate(
                                                () -> Transaction.builder()
                                                        .toAccount(account2)
                                                        .fromAccount(account1)
                                                        .amount(BigDecimal.valueOf(10000))
                                                        .build()
                                        )
                                        .limit(size / 2)
                        )
                        .collect(Collectors.toList());

        clientRepository.save(anyClient);
        accountRepository.saveAll(anyAccounts);
        transactionRepository.saveAll(transactions);

        return transactions;
    }

    @SneakyThrows
    private ResultActions getTransactionsRequest(Integer offset, Integer limit) {
        if (offset == DEFAULT_PARAM_VALUE) offset = 0;
        if (limit == DEFAULT_PARAM_VALUE) limit = 20;

        return mvc.perform(
                get(

                        "/accounts/{id}/transactions",
                        2147483648L
                )
                        .param("offset", offset.toString())
                        .param("limit", limit.toString())
        );
    }

    @SneakyThrows
    private void verify(ResultActions resultActions, List<Transaction> expected) {
        resultActions
                .andExpect(status().isOk())
                .andExpect(
                        content().json(
                                objectMapper.writeValueAsString(expected)
                        )
                );
    }
}
