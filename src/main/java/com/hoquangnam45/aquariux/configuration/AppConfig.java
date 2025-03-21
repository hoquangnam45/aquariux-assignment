package com.hoquangnam45.aquariux.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {
    @Bean
    public RestClient binanceRestClient() {
        return RestClient.builder().baseUrl("https://api.binance.com/api/v3/ticker/bookTicker").build();
    }

    @Bean
    public RestClient huobiRestClient() {
        return RestClient.builder().baseUrl("https://api.huobi.pro/market/tickers").build();
    }
}
