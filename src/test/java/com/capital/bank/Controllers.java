package com.capital.bank;

import com.capital.bank.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.function.ThrowingConsumer;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
@AllArgsConstructor
public class Controllers {
    public static final Integer DEFAULT_PARAM_VALUE = null;

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public ResultActions getAccountsRequest(String clientExternalId) {
        return mvc.perform(
                get(
                        "/clients/{id}/accounts",
                        clientExternalId
                )
        );
    }

    @SneakyThrows
    public ResultActions getTransactionsRequest(Integer offset, Integer limit) {
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
    public ResultActions postTransactionRequest(Transaction transaction) {
        return mvc.perform(
                post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction))
        );
    }

    @SneakyThrows
    public static void verify(ResultActions resultActions, ResultMatcher... matchers) {
        Arrays.stream(matchers)
                .forEach(
                        ThrowingConsumer.of(
                                resultActions::andExpect
                        )
                );
    }
}
