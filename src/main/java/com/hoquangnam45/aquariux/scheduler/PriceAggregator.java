package com.hoquangnam45.aquariux.scheduler;

import com.hoquangnam45.aquariux.entity.MarketPrice;
import com.hoquangnam45.aquariux.pojo.BinanceBookTicker;
import com.hoquangnam45.aquariux.pojo.HuobiResponse;
import com.hoquangnam45.aquariux.pojo.HuobiTicker;
import com.hoquangnam45.aquariux.repository.MarketPriceRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class PriceAggregator {
    private final RestClient binanceRestClient;
    private final RestClient huobiRestClient;
    private final MarketPriceRepository marketPriceRepository;

    public PriceAggregator(RestClient binanceRestClient, RestClient huobiRestClient, MarketPriceRepository marketPriceRepository) {
        this.huobiRestClient = huobiRestClient;
        this.binanceRestClient = binanceRestClient;
        this.marketPriceRepository = marketPriceRepository;
    }

    @Scheduled(timeUnit = TimeUnit.SECONDS, fixedRate = 10)
    public void aggregatePrices() {
        List<BinanceBookTicker> binanceTickersResponse = getBinanceBookTickers();
        HuobiResponse<List<HuobiTicker>> huobiTickersResponse = getHuobiTickers();

        List<BinanceBookTicker> binanceBookTickers = binanceTickersResponse == null ? Collections.emptyList() : binanceTickersResponse;
        List<HuobiTicker> huobiTickers;

        if (huobiTickersResponse.data() == null) {
            huobiTickers = Collections.emptyList();
        } else {
            huobiTickers = huobiTickersResponse.data();
        }

        LocalDateTime now = Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime();
        Map<String, MarketPrice> marketPrices = new HashMap<>();
        for (BinanceBookTicker binanceTicker : binanceBookTickers) {
            MarketPrice marketPrice = getMarketPrice(binanceTicker, now);
            marketPrices.put(marketPrice.getSymbol(), marketPrice);
        }
        for (HuobiTicker huobiTicker : huobiTickers) {
            MarketPrice marketPrice;
            if (!marketPrices.containsKey(huobiTicker.symbol().toUpperCase())) {
                marketPrice = getMarketPrice(huobiTicker, now);
            } else {
                marketPrice = marketPrices.get(huobiTicker.symbol().toUpperCase());
                if (marketPrice.getAskPrice().compareTo(huobiTicker.ask()) > 0) {
                    marketPrice.setAskPrice(huobiTicker.ask());
                }
                if (marketPrice.getBidPrice().compareTo(huobiTicker.bid()) < 0) {
                    marketPrice.setBidPrice(huobiTicker.bid());
                }
            }
            marketPrices.put(marketPrice.getSymbol(), marketPrice);
        }

        marketPriceRepository.saveAll(marketPrices.values());
    }

    private static MarketPrice getMarketPrice(BinanceBookTicker binanceTicker, LocalDateTime now) {
        MarketPrice marketPrice = new MarketPrice();
        String symbol = binanceTicker.symbol().toUpperCase();
        marketPrice.setBidPrice(binanceTicker.bidPrice());
        marketPrice.setAskPrice(binanceTicker.askPrice());
        marketPrice.setBidQty(binanceTicker.bidQty());
        marketPrice.setAskQty(binanceTicker.askQty());
        marketPrice.setSymbol(symbol);
        marketPrice.setUpdatedAt(now);
        return marketPrice;
    }

    private static MarketPrice getMarketPrice(HuobiTicker huobiTicker, LocalDateTime now) {
        String symbol = huobiTicker.symbol().toUpperCase();
        MarketPrice marketPrice = new MarketPrice();
        marketPrice.setBidPrice(huobiTicker.bid());
        marketPrice.setAskPrice(huobiTicker.ask());
        marketPrice.setBidQty(huobiTicker.bidSize());
        marketPrice.setAskQty(huobiTicker.askSize());
        marketPrice.setSymbol(symbol);
        marketPrice.setUpdatedAt(now);
        return marketPrice;
    }

    public List<BinanceBookTicker> getBinanceBookTickers() {
        return binanceRestClient.get().retrieve().body(new ParameterizedTypeReference<>() {
        });
    }

    public HuobiResponse<List<HuobiTicker>> getHuobiTickers() {
        return huobiRestClient.get().retrieve().body(new ParameterizedTypeReference<>() {
        });
    }
}
