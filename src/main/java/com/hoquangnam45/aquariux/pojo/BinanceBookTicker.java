package com.hoquangnam45.aquariux.pojo;

import java.math.BigDecimal;

public record BinanceBookTicker(String symbol, BigDecimal bidPrice, BigDecimal bidQty, BigDecimal askPrice,
                                BigDecimal askQty) {
}
