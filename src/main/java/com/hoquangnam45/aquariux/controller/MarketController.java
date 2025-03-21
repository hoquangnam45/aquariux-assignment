package com.hoquangnam45.aquariux.controller;

import com.hoquangnam45.aquariux.entity.MarketPrice;
import com.hoquangnam45.aquariux.pojo.MarketPriceResponse;
import com.hoquangnam45.aquariux.repository.MarketPriceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/market")
public class MarketController {
    private final MarketPriceRepository marketPriceRepository;

    public MarketController(MarketPriceRepository marketPriceRepository) {
        this.marketPriceRepository = marketPriceRepository;
    }

    @GetMapping("/price")
    public ResponseEntity<?> getBestPrice(@RequestParam String symbol) {
        MarketPrice v = marketPriceRepository.findBySymbol(symbol);
        return ResponseEntity.ok(new MarketPriceResponse(v.getSymbol(), v.getBidPrice(), v.getAskPrice(), OffsetDateTime.of(v.getUpdatedAt(), ZoneOffset.UTC).toInstant()));
    }
}
