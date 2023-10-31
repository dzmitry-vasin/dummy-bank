package com.capital.bank.config;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Component
public class DataGenerator {
    private static final long EXTERNAL_ID_VALUE_LEFT_BOUND = 1000000000L;
    private static final long EXTERNAL_ID_VALUE_RIGHT_BOUND = 9999999999L;

    private static final double BALANCE_VALUE_LEFT_BOUND = 10000;
    private static final double BALANCE_VALUE_RIGHT_BOUND = 99999;

    private final Random random = new Random();

    public List<String> clientExternalIds() {
        return List.of("Bill Gates", "Elon Musk", "Jeff Bezos");
    }

    public Long randomAccountExternalId() {
        return EXTERNAL_ID_VALUE_LEFT_BOUND +
                (long) (
                        random.nextDouble() * (EXTERNAL_ID_VALUE_RIGHT_BOUND - EXTERNAL_ID_VALUE_LEFT_BOUND)
                );
    }

    public BigDecimal randomBalance() {
        return BigDecimal.valueOf(
                BALANCE_VALUE_LEFT_BOUND +
                        random.nextDouble() * (BALANCE_VALUE_RIGHT_BOUND - BALANCE_VALUE_LEFT_BOUND)
        );
    }
}
