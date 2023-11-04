package com.capital.bank.repository;

import com.capital.bank.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends CrudRepository<Transaction, UUID> {
    List<Transaction> findAllByFromAccountExternalIdOrToAccountExternalIdOrderByTimestampDesc(
            Long fromAccountExternalId, Long toAccountExternalId, Pageable pageable
    );
}
