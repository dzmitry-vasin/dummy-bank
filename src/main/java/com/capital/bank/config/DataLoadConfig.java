package com.capital.bank.config;

import com.capital.bank.model.Account;
import com.capital.bank.model.Client;
import com.capital.bank.repository.AccountRepository;
import com.capital.bank.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Profile("!test")
public class DataLoadConfig {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final DataGenerator dataGenerator;

    @Bean
    public CommandLineRunner dataLoadRunner() {
        return args ->
                dataGenerator.clientExternalIds()
                        .forEach(
                                clientExternalId -> {
                                    Client client = Client.builder()
                                            .externalId(clientExternalId)
                                            .build();

                                    clientRepository.save(client);

                                    Account account = Account.builder()
                                            .client(client)
                                            .externalId(
                                                    dataGenerator.randomAccountExternalId()
                                            )
                                            .balance(
                                                    dataGenerator.randomBalance()
                                            )
                                            .build();

                                    accountRepository.save(account);
                                }
                        );
    }
}
