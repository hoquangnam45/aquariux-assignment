package com.hoquangnam45.aquariux.entity;

import com.hoquangnam45.aquariux.constant.Side;
import com.hoquangnam45.aquariux.constant.TradeStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    @Id
    private String id;

    private String orderId;

    private String symbol;

    private String currency;

    private String clientId;

    @Enumerated(EnumType.STRING)
    private Side side;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    private BigDecimal price;

    private BigDecimal qty;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}