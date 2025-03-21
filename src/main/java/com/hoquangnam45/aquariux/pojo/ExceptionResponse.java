package com.hoquangnam45.aquariux.pojo;

import java.time.Instant;

public record ExceptionResponse(String code, String path, String message, Object detail, Instant timestamp) {
}
