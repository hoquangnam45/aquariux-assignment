package com.hoquangnam45.aquariux.pojo;

import com.hoquangnam45.aquariux.constant.Side;
import com.hoquangnam45.aquariux.constant.TradeStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record TradeResponse(String id, String orderId, String symbol, String currency,
                            String clientId, Side side, TradeStatus status, BigDecimal price, BigDecimal qty,
                            Instant createdAt,
                            Instant updatedAt) {
}
