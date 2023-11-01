package com.capital.bank.service.data;

import com.capital.bank.model.Account;
import com.capital.bank.model.Client;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccountDataService {
    private static final long EXTERNAL_ID_VALUE_LEFT_BOUND = 2147483648L;
    private static final long EXTERNAL_ID_VALUE_RIGHT_BOUND = 9999999999L;

    private static final double BALANCE_VALUE_LEFT_BOUND = 10000;
    private static final double BALANCE_VALUE_RIGHT_BOUND = 99999;

    private final Random random = new Random();
    public List<Account> accounts(Client client) {
        return Stream.generate(
                        () -> account(client)
                )
                .limit(3)
                .collect(Collectors.toList());
    }

    private Account account(Client client) {
        return Account.builder()
                .client(client)
                .externalId(
                        randomAccountExternalId()
                )
                .balance(
                        randomBalance()
                )
                .build();
    }

    private Long randomAccountExternalId() {
        return EXTERNAL_ID_VALUE_LEFT_BOUND +
                (long) (
                        random.nextDouble() * (EXTERNAL_ID_VALUE_RIGHT_BOUND - EXTERNAL_ID_VALUE_LEFT_BOUND)
                );
    }

    private BigDecimal randomBalance() {
        return BigDecimal.valueOf(
                BALANCE_VALUE_LEFT_BOUND +
                        random.nextDouble() * (BALANCE_VALUE_RIGHT_BOUND - BALANCE_VALUE_LEFT_BOUND)
        );
    }
}
