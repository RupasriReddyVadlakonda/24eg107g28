package com.datavault.personal_data_vault.security;

import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import com.datavault.personal_data_vault.repository.ThirdPartyApplicationRepository;
import com.datavault.personal_data_vault.security.ApplicationPrincipal;
import com.datavault.personal_data_vault.security.CustomUserDetailsService;
import com.datavault.personal_data_vault.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import lombok.Generated;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter
extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ThirdPartyApplicationRepository applicationRepository;

    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
            return;
        }
        String token = authorization.substring(7);
        try {
            String type = this.jwtService.extractTokenType(token);
            if ("APPLICATION".equals(type)) {
                this.authenticateApplication(request, token);
            } else {
                this.authenticateUser(request, token);
            }
        }
        catch (Exception ignored) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
    }

    private void authenticateUser(HttpServletRequest request, String token) {
        String email = this.jwtService.extractUsername(token);
        if (email == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }
        UserDetails principal = this.userDetailsService.loadUserByUsername(email);
        if (principal.isEnabled() && !this.jwtService.isTokenExpired(token) && this.jwtService.isTokenValid(token, principal)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken((Object)principal, null, principal.getAuthorities());
            authentication.setDetails((Object)new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication((Authentication)authentication);
        }
    }

    private void authenticateApplication(HttpServletRequest request, String token) {
        if (SecurityContextHolder.getContext().getAuthentication() != null || this.jwtService.isTokenExpired(token)) {
            return;
        }
        Long appId = this.jwtService.extractApplicationId(token);
        if (appId == null) {
            return;
        }
        ThirdPartyApplication application = this.applicationRepository.findById(appId).orElse(null);
        if (application == null || !application.isActive() || !application.getClientId().equals(this.jwtService.extractUsername(token))) {
            return;
        }
        ApplicationPrincipal principal = new ApplicationPrincipal(application.getId(), application.getClientId());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken((Object)principal, null, List.of((GrantedAuthority & Serializable)() -> "ROLE_APPLICATION"));
        authentication.setDetails((Object)new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication((Authentication)authentication);
    }

    @Generated
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService, ThirdPartyApplicationRepository applicationRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.applicationRepository = applicationRepository;
    }
}

