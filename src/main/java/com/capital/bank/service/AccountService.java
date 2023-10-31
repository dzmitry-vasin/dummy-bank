package com.capital.bank.service;

import com.capital.bank.model.Account;
import com.capital.bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public List<Account> findAccounts(String clientExternalId) {
        return accountRepository.findAllByClientExternalId(clientExternalId);
    }
}
