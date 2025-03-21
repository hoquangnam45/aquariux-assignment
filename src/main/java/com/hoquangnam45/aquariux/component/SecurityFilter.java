package com.hoquangnam45.aquariux.component;

import com.hoquangnam45.aquariux.entity.Client;
import com.hoquangnam45.aquariux.repository.ClientRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final ClientRepository clientRepository;

    public SecurityFilter(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String encodedCredentials = authorizationHeader.substring("Basic ".length());
        String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials));
        String[] usernameAndPassword = decodedCredentials.split(":");
        if (usernameAndPassword.length != 2) {
            filterChain.doFilter(request, response);
            return;
        }
        Client user = clientRepository.findByUsername(usernameAndPassword[0]);
        if (user == null || !user.getPassword().equals(usernameAndPassword[1])) {
            filterChain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usernameAndPassword[0], usernameAndPassword[1], Collections.emptyList());
        authenticationToken.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
