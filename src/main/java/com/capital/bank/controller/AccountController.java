package com.capital.bank.controller;

import com.capital.bank.model.Account;
import com.capital.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients/{id}")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/accounts")
    public List<Account> findAccounts(@PathVariable("id") String clientExternalId) {
        return accountService.findAccounts(clientExternalId);
    }
}
