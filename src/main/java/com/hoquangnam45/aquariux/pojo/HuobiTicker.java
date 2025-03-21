package com.hoquangnam45.aquariux.pojo;

import java.math.BigDecimal;

public record HuobiTicker(String symbol, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close,
                          BigDecimal amount, BigDecimal vol, BigDecimal count, BigDecimal bid, BigDecimal bidSize,
                          BigDecimal ask, BigDecimal askSize) {
}
