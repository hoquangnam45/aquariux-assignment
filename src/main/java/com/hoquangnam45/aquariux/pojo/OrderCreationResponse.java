package com.hoquangnam45.aquariux.pojo;

import com.hoquangnam45.aquariux.constant.OrderStatus;
import com.hoquangnam45.aquariux.constant.OrderType;
import com.hoquangnam45.aquariux.constant.Side;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderCreationResponse(String id, String symbol, Side side, OrderType type,
                                    OrderStatus status, BigDecimal qty, BigDecimal filledQty, BigDecimal price,
                                    String clientId, String currency, Instant createdAt,
                                    Instant updatedAt) {
}
