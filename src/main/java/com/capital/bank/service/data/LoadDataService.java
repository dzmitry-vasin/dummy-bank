package com.capital.bank.service.data;

import com.capital.bank.model.Account;
import com.capital.bank.model.Client;
import com.capital.bank.repository.AccountRepository;
import com.capital.bank.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LoadDataService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountDataService accountDataService;
    private final ClientDataService clientDataService;

    public void execute() {
        List<Client> clients = clientDataService.clients();
        clientRepository.saveAll(clients);

        List<Account> accounts = clients.stream()
                .flatMap(client -> accountDataService.accounts(client).stream())
                .collect(Collectors.toList());
        accountRepository.saveAll(accounts);
    }
}
