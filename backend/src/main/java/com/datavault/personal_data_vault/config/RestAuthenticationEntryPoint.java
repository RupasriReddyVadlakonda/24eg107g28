package com.datavault.personal_data_vault.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.datavault.personal_data_vault.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import lombok.Generated;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class RestAuthenticationEntryPoint
implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(401);
        response.setContentType("application/json");
        this.objectMapper.writeValue((OutputStream)response.getOutputStream(), ApiResponse.error("Authentication required", "AUTHENTICATION_REQUIRED"));
    }

    @Generated
    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}

