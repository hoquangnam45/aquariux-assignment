package com.hoquangnam45.aquariux.pojo;

import java.math.BigDecimal;

public record AccountBalanceResponse(BigDecimal balance, String currency) {
}
