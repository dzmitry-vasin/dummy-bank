package com.capital.bank.repository;

import com.capital.bank.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends CrudRepository<Account, UUID> {
    List<Account> findAllByClientExternalId(String externalID);

    Account findByExternalId(Long externalId);
}
