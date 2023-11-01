package com.capital.bank.config;

import com.capital.bank.service.data.LoadDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Profile("!test")
public class LoadDataConfig {
    private final LoadDataService loadDataService;

    @Bean
    public CommandLineRunner dataLoadRunner() {
        return args -> loadDataService.execute();
    }
}
