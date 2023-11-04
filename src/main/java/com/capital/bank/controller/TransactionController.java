package com.capital.bank.controller;

import com.capital.bank.model.Transaction;
import com.capital.bank.service.TransactionService;
import com.capital.bank.service.exception.UnprocessableTransactionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/accounts/{id}/transactions")
    public List<Transaction> findTransactions(
            @PathVariable("id") Long accountExternalId,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "20") int limit
    ) {
        return transactionService.findAll(accountExternalId, offset, limit);
    }

    @PostMapping(value = "transactions")
    @ResponseStatus(HttpStatus.OK)
    public void save(@RequestBody Transaction transaction) {
        transactionService.save(transaction);
    }

    @ExceptionHandler(UnprocessableTransactionException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public void handleException(UnprocessableTransactionException ex) {
        log.error(ex.getMessage());
    }
}
