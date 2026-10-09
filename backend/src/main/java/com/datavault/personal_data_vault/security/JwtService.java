package com.datavault.personal_data_vault.security;

import com.datavault.personal_data_vault.entity.ThirdPartyApplication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SecretKey signingKey;
    private final long accessTokenExpiration;

    public JwtService(@Value(value="${app.jwt.secret}") String base64Secret, @Value(value="${app.jwt.access-token-expiration}") long accessTokenExpiration) {
        byte[] key = Decoders.BASE64.decode(base64Secret);
        if (key.length < 32) {
            throw new IllegalArgumentException("JWT_SECRET must decode to at least 32 bytes");
        }
        this.signingKey = Keys.hmacShaKeyFor((byte[])key);
        if (accessTokenExpiration < 60000L) {
            throw new IllegalArgumentException("Access-token lifetime must be at least one minute");
        }
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public String generateAccessToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return this.buildToken(extraClaims, userDetails.getUsername(), this.accessTokenExpiration);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return this.generateAccessToken(userDetails, Map.of());
    }

    public String generateApplicationToken(ThirdPartyApplication application) {
        return this.buildToken(Map.of("tokenType", "APPLICATION", "applicationId", application.getId()), application.getClientId(), this.accessTokenExpiration);
    }

    private String buildToken(Map<String, Object> claims, String subject, long lifetimeMillis) {
        Instant now = Instant.now();
        return Jwts.builder().claims(claims).subject(subject).issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(lifetimeMillis))).signWith((Key)this.signingKey).compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return this.extractUsername(token).equals(userDetails.getUsername()) && !this.isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    public String extractTokenType(String token) {
        return this.extractClaim(token, claims -> (String)claims.get("tokenType", String.class));
    }

    public Long extractApplicationId(String token) {
        Long l;
        Object value = this.extractAllClaims(token).get((Object)"applicationId");
        if (value instanceof Number) {
            Number number = (Number)value;
            l = number.longValue();
        } else {
            l = null;
        }
        return l;
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(this.extractAllClaims(token));
    }

    public Claims extractAllClaims(String token) {
        return (Claims)Jwts.parser().verifyWith(this.signingKey).build().parseSignedClaims((CharSequence)token).getPayload();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = this.extractClaim(token, Claims::getExpiration);
        return expiration == null || expiration.before(new Date());
    }

    public long getAccessTokenExpirationMillis() {
        return this.accessTokenExpiration;
    }
}
