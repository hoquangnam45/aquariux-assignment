package com.hoquangnam45.aquariux.pojo;

import com.hoquangnam45.aquariux.constant.OrderType;
import com.hoquangnam45.aquariux.constant.Side;

import java.math.BigDecimal;

public record OrderCreationRequest(String symbol, Side side, OrderType type,
                                   BigDecimal qty,
                                   BigDecimal price, String currency) {
}
