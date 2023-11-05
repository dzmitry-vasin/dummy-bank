package com.capital.bank.controller;

import com.capital.bank.Controllers;
import com.capital.bank.IntegrationTest;
import com.capital.bank.TestData;
import com.capital.bank.model.Account;
import com.capital.bank.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static com.capital.bank.Controllers.DEFAULT_PARAM_VALUE;
import static com.capital.bank.Controllers.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AllArgsConstructor
public class TransactionControllerTest {
    private final Controllers controllers;
    private final ObjectMapper objectMapper;

    private final TestData testData;

    @Test
    @SneakyThrows
    public void givenAccountsWithEqualTo20TransactionsBetween_whenGetTransactions_thenResponseWithAllTransactions() {
        List<Transaction> transactions = testData.prepareTransactions(20);

        verifyTransactions(
                controllers.getTransactionsRequest(DEFAULT_PARAM_VALUE, DEFAULT_PARAM_VALUE),
                transactions
        );
    }

    @Test
    @SneakyThrows
    public void givenAccountsWithMoreThan20TransactionsBetween_whenGetTransactions_thenResponseWith20OnlyTransactions() {
        List<Transaction> transactions = testData.prepareTransactions(30);
        List<Transaction> expectedTransactions = transactions.stream()
                .sorted(
                        Comparator.comparing(Transaction::getTimestamp).reversed()
                )
                .limit(20)
                .toList();

        verifyTransactions(
                controllers.getTransactionsRequest(DEFAULT_PARAM_VALUE, DEFAULT_PARAM_VALUE),
                expectedTransactions
        );
    }

    @Test
    @SneakyThrows
    public void givenAccountsWithMoreThan20AndLessThan40TransactionsBetween_whenGetTransactionsWithOffset_thenResponseWithRemainingTransactions() {
        List<Transaction> transactions = testData.prepareTransactions(30);
        List<Transaction> expectedTransactions = transactions.stream()
                .sorted(
                        Comparator.comparing(Transaction::getTimestamp).reversed()
                )
                .skip(20)
                .toList();

        verifyTransactions(
                controllers.getTransactionsRequest(1, DEFAULT_PARAM_VALUE),
                expectedTransactions
        );
    }

    @Test
    @SneakyThrows
    public void givenAccountsWithLessThan40TransactionsBetween_whenGetTransactionsWithLimit40_thenResponseWithAllTransactions() {
        List<Transaction> transactions = testData.prepareTransactions(30);

        verifyTransactions(
                controllers.getTransactionsRequest(DEFAULT_PARAM_VALUE, 40),
                transactions
        );
    }

    @Test
    @SneakyThrows
    public void givenAccounts_whenPostTransactionWithAmountLessThanBalance_thenSuccessfulResponse() {
        List<Account> anyAccounts = testData.prepareAccounts("any");
        Transaction transaction = Transaction.builder()
                .fromAccount(anyAccounts.get(0))
                .toAccount(anyAccounts.get(1))
                .amount(BigDecimal.valueOf(5000))
                .build();

        verify(
                controllers.postTransactionRequest(transaction),
                status().isOk()
        );
    }

    @Test
    @SneakyThrows
    public void givenAccounts_whenPostTransactionWithAmountMoreThanBalance_thenErrorResponse() {
        List<Account> anyAccounts = testData.prepareAccounts("any");

        Transaction transaction = Transaction.builder()
                .fromAccount(anyAccounts.get(0))
                .toAccount(anyAccounts.get(1))
                .amount(BigDecimal.valueOf(15000))
                .build();

        verify(
                controllers.postTransactionRequest(transaction),
                status().isUnprocessableEntity()
        );
    }

    @Test
    @SneakyThrows
    public void givenAccounts_whenPostTransactionWithLessThanBalanceWithinTheSameAccount_thenErrorResponse() {
        List<Account> anyAccounts = testData.prepareAccounts("any");

        Transaction transaction = Transaction.builder()
                .fromAccount(anyAccounts.get(0))
                .toAccount(anyAccounts.get(0))
                .amount(BigDecimal.valueOf(5000))
                .build();

        verify(
                controllers.postTransactionRequest(transaction),
                status().isUnprocessableEntity()
        );
    }

    @SneakyThrows
    private void verifyTransactions(ResultActions resultActions, List<Transaction> expected) {
        verify(
                resultActions,
                status().isOk(),
                content().json(
                        objectMapper.writeValueAsString(expected)
                )
        );
    }
}
