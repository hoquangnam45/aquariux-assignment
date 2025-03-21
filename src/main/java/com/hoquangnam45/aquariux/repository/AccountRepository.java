package com.hoquangnam45.aquariux.repository;

import com.hoquangnam45.aquariux.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByClientIdAndCurrency(String clientId, String currency);
}
