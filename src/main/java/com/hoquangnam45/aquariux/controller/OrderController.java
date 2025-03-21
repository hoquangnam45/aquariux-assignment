package com.hoquangnam45.aquariux.controller;

import com.hoquangnam45.aquariux.constant.OrderStatus;
import com.hoquangnam45.aquariux.constant.OrderType;
import com.hoquangnam45.aquariux.entity.Account;
import com.hoquangnam45.aquariux.entity.Client;
import com.hoquangnam45.aquariux.entity.Order;
import com.hoquangnam45.aquariux.pojo.ExceptionResponse;
import com.hoquangnam45.aquariux.pojo.OrderCreationRequest;
import com.hoquangnam45.aquariux.pojo.OrderCreationResponse;
import com.hoquangnam45.aquariux.repository.AccountRepository;
import com.hoquangnam45.aquariux.repository.OrderRepository;
import com.hoquangnam45.aquariux.scheduler.OrderExecutor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final AccountRepository accountRepository;
    private final OrderRepository orderRepository;
    private final OrderExecutor orderExecutor;

    public OrderController(AccountRepository accountRepository, OrderRepository orderRepository, OrderExecutor orderExecutor) {
        this.accountRepository = accountRepository;
        this.orderRepository = orderRepository;
        this.orderExecutor = orderExecutor;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreationRequest request, HttpServletRequest req) {
        if (!request.symbol().equals("ETHUSDT") && !request.symbol().equals("BTCUSDT")) {
            return ResponseEntity.badRequest().body(new ExceptionResponse("400", req.getServletPath(), "Invalid symbol", null, Instant.now()));
        }

        Client authenticatedClient = (Client) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Account account = accountRepository.findByClientIdAndCurrency(authenticatedClient.getId(), request.currency());
        if (account == null) {
            return ResponseEntity.badRequest().body(new ExceptionResponse("400", req.getServletPath(), "Account not found for client and currency", null, Instant.now()));
        }
        if (request.type() != OrderType.MARKET) {
            return ResponseEntity.badRequest().body(new ExceptionResponse("400", req.getServletPath(), "Only market orders are supported", null, Instant.now()));
        }
        Order order = new Order(UUID.randomUUID().toString(), request.symbol(), request.side(), request.type(), OrderStatus.NEW, request.qty(), BigDecimal.ZERO, request.price(), authenticatedClient.getId(), request.currency(), Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime(), Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime());
        order = orderRepository.save(order);
        orderExecutor.addOrder(order);
        return ResponseEntity.ok(new OrderCreationResponse(order.getId(), order.getSymbol(), order.getSide(), order.getType(), order.getStatus(), order.getQty(), order.getFilledQty(), order.getPrice(), order.getClientId(), order.getCurrency(), OffsetDateTime.of(order.getCreatedAt(), ZoneOffset.UTC).toInstant(), OffsetDateTime.of(order.getUpdatedAt(), ZoneOffset.UTC).toInstant()));
    }
}
