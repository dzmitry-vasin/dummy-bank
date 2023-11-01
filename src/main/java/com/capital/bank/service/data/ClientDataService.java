package com.capital.bank.service.data;

import com.capital.bank.model.Client;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientDataService {
    private static final List<String> EXTERNAL_IDS = List.of("Bill Gates", "Elon Musk", "Jeff Bezos");

    public List<Client> clients() {
        return EXTERNAL_IDS.stream()
                .map(this::client)
                .collect(Collectors.toList());
    }

    private Client client(String clientExternalId) {
        return Client.builder()
                .externalId(clientExternalId)
                .build();
    }
}
