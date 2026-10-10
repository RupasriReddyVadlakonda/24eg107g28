package com.datavault.personal_data_vault.security;

import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import com.datavault.personal_data_vault.repository.ThirdPartyApplicationRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ThirdPartyApplicationRepository applicationRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);
        try {
            String tokenType = jwtService.extractTokenType(token);
            if ("APPLICATION".equals(tokenType)) {
                authenticateApplication(request, token);
            } else if ("USER".equals(tokenType)) {
                authenticateUser(request, token);
            }
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException invalidToken) {
            // Leave the request unauthenticated; protected routes are rejected by Spring Security.
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(HttpServletRequest request, String token) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        String email = jwtService.extractUsername(token);
        UserDetails principal = userDetailsService.loadUserByUsername(email);
        if (!principal.isEnabled() || !jwtService.isTokenValid(token, principal)) {
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void authenticateApplication(HttpServletRequest request, String token) {
        if (SecurityContextHolder.getContext().getAuthentication() != null || jwtService.isTokenExpired(token)) {
            return;
        }

        Long applicationId = jwtService.extractApplicationId(token);
        if (applicationId == null) {
            return;
        }

        ThirdPartyApplication application = applicationRepository.findById(applicationId).orElse(null);
        if (application == null || !application.isActive()
                || !application.getClientId().equals(jwtService.extractUsername(token))) {
            return;
        }

        ApplicationPrincipal principal = new ApplicationPrincipal(application.getId(), application.getClientId());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal, null, List.of(() -> "ROLE_APPLICATION"));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService,
            ThirdPartyApplicationRepository applicationRepository
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.applicationRepository = applicationRepository;
    }
}
