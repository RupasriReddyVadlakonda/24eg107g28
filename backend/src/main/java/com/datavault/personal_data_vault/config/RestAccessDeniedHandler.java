package com.datavault.personal_data_vault.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.datavault.personal_data_vault.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import lombok.Generated;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class RestAccessDeniedHandler
implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(403);
        response.setContentType("application/json");
        this.objectMapper.writeValue((OutputStream)response.getOutputStream(), ApiResponse.error("Access denied", "ACCESS_DENIED"));
    }

    @Generated
    public RestAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}

