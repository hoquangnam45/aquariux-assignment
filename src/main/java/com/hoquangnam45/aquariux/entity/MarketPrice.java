package com.hoquangnam45.aquariux.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "market_price")
public class MarketPrice {
    @Id
    private String symbol;

    private BigDecimal bidPrice;

    private BigDecimal askPrice;

    private BigDecimal bidQty;

    private BigDecimal askQty;

    private LocalDateTime updatedAt;
}
