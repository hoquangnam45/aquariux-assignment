package com.hoquangnam45.aquariux.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "position")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(Position.PositionId.class)
public class Position {
    @Id
    private String clientId;

    @Id
    private String symbol;

    @Id
    private String currency;

    private BigDecimal qty;

    private BigDecimal amt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class PositionId implements Serializable {
        private String clientId;
        private String symbol;
        private String currency;
    }
}