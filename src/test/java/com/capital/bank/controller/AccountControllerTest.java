package com.capital.bank.controller;

import com.capital.bank.IntegrationTest;
import com.capital.bank.model.Account;
import com.capital.bank.model.Client;
import com.capital.bank.repository.AccountRepository;
import com.capital.bank.repository.ClientRepository;
import com.capital.bank.service.data.LoadDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.function.ThrowingConsumer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AllArgsConstructor
public class AccountControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;

    private final LoadDataService loadDataService;

    @Test
    @SneakyThrows
    public void givenAnyClient_whenGetAnyAccounts_thenValidResponse() {
        String anyClientExternalId = "any";

        Client anyClient = Client.builder()
                .externalId(anyClientExternalId)
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

        getAccountsAndVerify(
                anyClientExternalId,
                status().isOk(),
                content().json(
                        objectMapper.writeValueAsString(anyAccounts),
                        false
                )
        );
    }

    @Test
    @SneakyThrows
    public void givenNoAnyClient_whenGetAnyAccounts_thenEmptyResponse() {
        Client noAnyClient = Client.builder()
                .externalId("noAny")
                .build();

        Account account1 = Account.builder()
                .client(noAnyClient)
                .externalId(2147483648L)
                .balance(BigDecimal.valueOf(10000))
                .build();

        Account account2 = Account.builder()
                .client(noAnyClient)
                .externalId(9999999999L)
                .balance(BigDecimal.valueOf(99999))
                .build();

        List<Account> noAnyAccounts = List.of(account1, account2);

        clientRepository.save(noAnyClient);
        accountRepository.saveAll(noAnyAccounts);

        String anyClientExternalId = "any";
        List<Account> anyAccounts = List.of();

        getAccountsAndVerify(
                anyClientExternalId,
                status().isOk(),
                content().json(
                        objectMapper.writeValueAsString(anyAccounts),
                        false
                )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"Bill Gates", "Elon Musk", "Jeff Bezos"})
    public void givenGeneratedClients_whenGetGeneratedAccounts_thenValidResponse(String clientExternalId) {
        loadDataService.execute();

        getAccountsAndVerify(
                clientExternalId,
                status().isOk(),
                jsonPath("$", hasSize(3)),
                jsonPath(
                        "$[*].externalId",
                        everyItem(
                                greaterThan(2147483648L)
                        )
                ),
                jsonPath(
                        "$[*].externalId",
                        everyItem(
                                lessThan(9999999999L)
                        )
                ),
                jsonPath(
                        "$[*].balance",
                        everyItem(
                                greaterThan(10000)
                        )
                ),
                jsonPath(
                        "$[*].balance",
                        everyItem(
                                lessThan(99999))
                ),
                jsonPath(
                        "$[*].client.externalId",
                        everyItem(is(clientExternalId))
                )
        );
    }

    @SneakyThrows
    private ResultActions getAccounts(String clientExternalId) {
        return mvc.perform(
                get(
                        "/clients/{id}/accounts",
                        clientExternalId
                )
        );
    }

    @SneakyThrows
    private void getAccountsAndVerify(String clientExternalId, ResultMatcher... matchers) {
        ResultActions resultActions = getAccounts(clientExternalId);
        Arrays.stream(matchers)
                .forEach(
                        ThrowingConsumer.of(
                                resultActions::andExpect
                        )
                );
    }
}
