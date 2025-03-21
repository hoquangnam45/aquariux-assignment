package com.hoquangnam45.aquariux.controller;

import com.hoquangnam45.aquariux.constant.Side;
import com.hoquangnam45.aquariux.entity.Client;
import com.hoquangnam45.aquariux.entity.Trade;
import com.hoquangnam45.aquariux.pojo.TradeResponse;
import com.hoquangnam45.aquariux.repository.TradeRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/trade")
public class TradeController {
    private final TradeRepository tradeRepository;

    public TradeController(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @GetMapping("/history")
    public ResponseEntity<?> getTradeHistory(
            @RequestParam(required = false) String symbol,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) Side side,
            @RequestParam(required = false) OffsetDateTime from,
            @RequestParam(required = false) OffsetDateTime to) {
        Client authenticatedClient = (Client) SecurityContextHolder.getContext().getAuthentication().getDetails();
        List<Specification<Trade>> filters = new ArrayList<>();
        filters.add((root, query, cb) -> cb.equal(root.get("clientId"), authenticatedClient.getId()));
        if (symbol != null) {
            filters.add((root, query, cb) -> cb.equal(root.get("symbol"), symbol));
        }
        if (currency != null) {
            filters.add((root, query, cb) -> cb.equal(root.get("currency"), currency));
        }
        if (orderId != null) {
            filters.add((root, query, cb) -> cb.equal(root.get("orderId"), orderId));
        }
        if (side != null) {
            filters.add((root, query, cb) -> cb.equal(root.get("side"), side));
        }
        if (from != null) {
            filters.add((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), from));
        }
        if (to != null) {
            filters.add((root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), to));
        }
        return ResponseEntity.ok(tradeRepository.findAll(Specification.allOf(filters)).stream().map(v -> new TradeResponse(v.getId(), v.getOrderId(), v.getSymbol(), v.getCurrency(), v.getClientId(), v.getSide(), v.getStatus(), v.getPrice(), v.getQty(), OffsetDateTime.of(v.getCreatedAt(), ZoneOffset.UTC).toInstant(), OffsetDateTime.of(v.getUpdatedAt(), ZoneOffset.UTC).toInstant())).toList()); // Placeholder response
    }
}