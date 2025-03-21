package com.hoquangnam45.aquariux.controller;

import com.hoquangnam45.aquariux.entity.Account;
import com.hoquangnam45.aquariux.entity.Client;
import com.hoquangnam45.aquariux.pojo.AccountBalanceResponse;
import com.hoquangnam45.aquariux.pojo.ExceptionResponse;
import com.hoquangnam45.aquariux.repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;

@RestController
@RequestMapping("/account")
public class ClientController {
    private final AccountRepository accountRepository;

    public ClientController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/balance/{currency}")
    public ResponseEntity<?> getAccountBalance(@PathVariable("currency") String currency, HttpServletRequest request) {
        Client authenticatedClient = (Client) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Account account = accountRepository.findByClientIdAndCurrency(authenticatedClient.getId(), currency);
        if (account == null) {
            return ResponseEntity.badRequest().body(new ExceptionResponse("400", request.getServletPath(), "Account not found for client and currency", null, Instant.now()));
        }
        BigDecimal accountBalance = account.getBalance();
        return ResponseEntity.ok(new AccountBalanceResponse(accountBalance, currency));
    }
}
