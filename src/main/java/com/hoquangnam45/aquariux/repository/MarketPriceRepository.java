package com.hoquangnam45.aquariux.repository;

import com.hoquangnam45.aquariux.entity.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPrice, String>, JpaSpecificationExecutor<MarketPrice> {
    MarketPrice findBySymbol(String symbol);
}
