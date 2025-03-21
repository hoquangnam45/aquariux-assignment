package com.hoquangnam45.aquariux.entity;

import com.hoquangnam45.aquariux.constant.OrderStatus;
import com.hoquangnam45.aquariux.constant.OrderType;
import com.hoquangnam45.aquariux.constant.Side;
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
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private String id;

    private String symbol;

    @Enumerated(EnumType.STRING)
    private Side side;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal qty;

    private BigDecimal filledQty;

    private BigDecimal price;

    private String clientId;

    private String currency;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}