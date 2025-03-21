package com.hoquangnam45.aquariux.pojo;

public record HuobiResponse<T>(T data, String status, Long ts) {
}
