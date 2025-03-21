package com.hoquangnam45.aquariux.pojo;

import java.math.BigDecimal;
import java.time.Instant;

public record MarketPriceResponse(String symbol, BigDecimal bidPrice, BigDecimal askPrice, Instant updatedAt) {
}
