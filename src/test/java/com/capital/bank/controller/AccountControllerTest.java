package com.capital.bank.controller;

import com.capital.bank.Controllers;
import com.capital.bank.IntegrationTest;
import com.capital.bank.TestData;
import com.capital.bank.model.Account;
import com.capital.bank.service.data.LoadDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.capital.bank.Controllers.verify;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AllArgsConstructor
public class AccountControllerTest {
    private final Controllers controllers;
    private final ObjectMapper objectMapper;

    private final TestData testData;

    private final LoadDataService loadDataService;

    @Test
    @SneakyThrows
    public void givenAnyClient_whenGetAnyAccounts_thenValidResponse() {
        String anyClientExternalId = "any";
        List<Account> anyAccounts = testData.prepareAccounts(anyClientExternalId);

        verify(
                controllers.getAccountsRequest(anyClientExternalId),
                status().isOk(),
                content().json(
                        objectMapper.writeValueAsString(anyAccounts)
                )
        );
    }

    @Test
    @SneakyThrows
    public void givenNoAnyClient_whenGetAnyAccounts_thenEmptyResponse() {
        testData.prepareAccounts("noAny");

        String anyClientExternalId = "any";
        List<Account> anyAccounts = List.of();

        verify(
                controllers.getAccountsRequest(anyClientExternalId),
                status().isOk(),
                content().json(
                        objectMapper.writeValueAsString(anyAccounts)
                )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"Bill Gates", "Elon Musk", "Jeff Bezos"})
    public void givenGeneratedClients_whenGetGeneratedAccounts_thenValidResponse(String clientExternalId) {
        loadDataService.execute();

        verify(
                controllers.getAccountsRequest(clientExternalId),
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
}
