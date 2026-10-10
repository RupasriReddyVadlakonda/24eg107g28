package com.datavault.personal_data_vault.security;

import com.datavault.personal_data_vault.entity.User;
import com.datavault.personal_data_vault.security.JwtService;
import com.datavault.personal_data_vault.security.UserPrincipal;
import io.jsonwebtoken.io.Encoders;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {
    JwtServiceTest() {
    }

    @Test
    void signsAndValidatesUserClaims() {
        String secret = Encoders.BASE64.encode(new byte[32]);
        JwtService service = new JwtService(secret, 900000L);
        UserPrincipal principal = new UserPrincipal(Long.valueOf(42L), "user@example.test", "hash", User.Role.USER, true);
        String token = service.generateAccessToken((UserDetails)principal, Map.of("userId", 42L, "role", "USER", "tokenType", "USER"));
        Assertions.assertTrue((boolean)service.isTokenValid(token, (UserDetails)principal));
        Assertions.assertEquals((Object)"user@example.test", (Object)service.extractUsername(token));
        Assertions.assertEquals((Object)"USER", (Object)service.extractTokenType(token));
        Assertions.assertEquals((Long)42L, (Long)((Long)service.extractClaim(token, claims -> ((Number)claims.get((Object)"userId")).longValue())));
        Assertions.assertFalse((boolean)service.isTokenValid(token, (UserDetails)new UserPrincipal(Long.valueOf(2L), "other@example.test", "hash", User.Role.USER, true)));
    }

    @Test
    void principalStringRepresentationDoesNotExposePasswordHash() {
        UserPrincipal principal = new UserPrincipal(42L, "user@example.test", "encoded-password-hash", User.Role.USER, true);

        Assertions.assertFalse(principal.toString().contains("encoded-password-hash"));
        Assertions.assertTrue(principal.toString().contains("[REDACTED]"));
    }

    @Test
    void rejectsWeakJwtKey() {
        String secret = Encoders.BASE64.encode(new byte[16]);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new JwtService(secret, 900000L));
    }
}
