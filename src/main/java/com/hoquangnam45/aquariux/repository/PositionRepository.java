package com.hoquangnam45.aquariux.repository;

import com.hoquangnam45.aquariux.entity.Position;
import com.hoquangnam45.aquariux.entity.Position.PositionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, PositionId>, JpaSpecificationExecutor<Position> {
    Position findByClientIdAndCurrencyAndSymbol(String clientId, String currency, String symbol);
}
