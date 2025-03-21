package com.hoquangnam45.aquariux.scheduler;

import com.hoquangnam45.aquariux.constant.OrderStatus;
import com.hoquangnam45.aquariux.constant.OrderType;
import com.hoquangnam45.aquariux.constant.Side;
import com.hoquangnam45.aquariux.constant.TradeStatus;
import com.hoquangnam45.aquariux.entity.Account;
import com.hoquangnam45.aquariux.entity.MarketPrice;
import com.hoquangnam45.aquariux.entity.Order;
import com.hoquangnam45.aquariux.entity.Position;
import com.hoquangnam45.aquariux.entity.Trade;
import com.hoquangnam45.aquariux.repository.AccountRepository;
import com.hoquangnam45.aquariux.repository.MarketPriceRepository;
import com.hoquangnam45.aquariux.repository.OrderRepository;
import com.hoquangnam45.aquariux.repository.PositionRepository;
import com.hoquangnam45.aquariux.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Slf4j
public class OrderExecutor {
    private final LinkedBlockingQueue<Order> orderQueue;
    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;
    private final MarketPriceRepository marketPriceRepository;
    private final AccountRepository accountRepository;
    private final PositionRepository positionRepository;

    public OrderExecutor(OrderRepository orderRepository, TradeRepository tradeRepository, MarketPriceRepository marketPriceRepository, AccountRepository accountRepository, PositionRepository positionRepository) {
        this.orderRepository = orderRepository;
        this.tradeRepository = tradeRepository;
        this.marketPriceRepository = marketPriceRepository;
        this.positionRepository = positionRepository;
        this.orderQueue = new LinkedBlockingQueue<>();
        this.accountRepository = accountRepository;
        processOrders();
    }

    public void addOrder(Order order) {
        orderQueue.offer(order);
    }

    public void processOrders() {
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                Order order = orderQueue.poll();
                if (order != null) {
                    try {
                        executeOrder(order);
                    } catch (Exception e) {
                        log.error("Error processing order: {}", e.getMessage(), e);
                    }
                }
            }
        });
    }

    public void executeOrder(Order order) {
        try {
            if (order.getStatus() != OrderStatus.NEW && order.getStatus() != OrderStatus.IN_PROGRESS) {
                return; // Only process pending orders
            }
            if (order.getType() == OrderType.MARKET) {
                executeMarketOrder(order);
            }
        } catch (Exception e) {
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            throw e; // Re-throw the exception for the caller to handle
        }
    }

    private void executeMarketOrder(Order order) {
        MarketPrice marketPrice = marketPriceRepository.findBySymbol(order.getSymbol());
        if (marketPrice == null) {
            throw new IllegalStateException("Market price not found for symbol: " + order.getSymbol());
        }
        BigDecimal bestPrice;
        if (order.getSide() == Side.BUY) {
            bestPrice = marketPrice.getAskPrice();
        } else {
            bestPrice = marketPrice.getBidPrice();
        }

        Account account = accountRepository.findByClientIdAndCurrency(order.getClientId(), order.getCurrency());
        if (account == null) {
            throw new IllegalStateException("Account not found for client: " + order.getClientId() + ", currency: " + order.getCurrency());
        }

        BigDecimal executePrice = bestPrice;
        BigDecimal executeQty;
        BigDecimal executeAmt;
        BigDecimal positionQty;
        BigDecimal positionAmt;
        if (order.getSide() == Side.BUY) {
            BigDecimal accountBalance = account.getBalance();
            if (accountBalance.compareTo(BigDecimal.ZERO) < 0) {
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
                return;
            }
            BigDecimal remainingQty = order.getQty().subtract(order.getFilledQty());
            if (accountBalance.compareTo(remainingQty.multiply(bestPrice)) < 0) {
                executeQty = accountBalance.divide(bestPrice, 2, RoundingMode.FLOOR);
                order.setStatus(OrderStatus.PARTIALLY_FILLED);
            } else {
                executeQty = remainingQty;
                order.setStatus(OrderStatus.FILLED);
            }
            executeAmt = executeQty.multiply(bestPrice);
            positionAmt = executeAmt;
            positionQty = executeQty;
        } else {
            executeQty = order.getQty().subtract(order.getFilledQty());
            executeAmt = executeQty.multiply(bestPrice);
            positionAmt = executeAmt.negate();
            positionQty = executeQty.negate();
            order.setFilledQty(order.getFilledQty().add(executeQty));
            order.setStatus(OrderStatus.FILLED);
        }
        account.setBalance(account.getBalance().subtract(positionAmt));
        LocalDateTime now = Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime();
        Trade trade = new Trade(UUID.randomUUID().toString(), order.getId(), order.getSymbol(), order.getCurrency(), order.getClientId(), order.getSide(), TradeStatus.SUCCESS, executePrice, executeQty, now, now);
        Position position = positionRepository.findByClientIdAndCurrencyAndSymbol(order.getClientId(), order.getCurrency(), order.getSymbol());
        if (position == null) {
            position = new Position(order.getClientId(), order.getSymbol(), order.getCurrency(), executeQty, executeAmt);
        } else {
            position.setQty(position.getQty().add(positionQty));
            position.setAmt(position.getAmt().add(positionAmt));
        }

        accountRepository.save(account);
        positionRepository.save(position);
        tradeRepository.save(trade);
        orderRepository.save(order);
    }
}
