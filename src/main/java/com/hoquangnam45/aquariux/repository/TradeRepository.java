package com.hoquangnam45.aquariux.repository;

import com.hoquangnam45.aquariux.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, String>, JpaSpecificationExecutor<Trade> {
}
