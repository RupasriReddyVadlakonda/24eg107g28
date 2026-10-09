package com.datavault.personal_data_vault.redis;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
    @Value(value="${app.rate-limit.max-requests}")
    private int maxRequests;
    @Value(value="${app.rate-limit.window-seconds}")
    private int windowSeconds;
    @Value(value="${app.security-alert.window-seconds}")
    private int alertWindowSeconds;
    private final ConcurrentHashMap<String, Window> rateLimitWindows = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, Window> unauthorizedWindows = new ConcurrentHashMap();

    public boolean isAllowed(String key) {
        Window window = this.getOrCreateWindow(this.rateLimitWindows, key, this.windowSeconds);
        return window.count().incrementAndGet() <= (long)this.maxRequests;
    }

    public long incrementUnauthorizedCounter(String key) {
        Window window = this.getOrCreateWindow(this.unauthorizedWindows, key, this.alertWindowSeconds);
        return window.count().incrementAndGet();
    }

    public long getRateLimitCount(String key) {
        Window window = this.rateLimitWindows.get("rate_limit:" + key);
        if (window == null || Instant.now().isAfter(window.expiresAt())) {
            return 0L;
        }
        return window.count().get();
    }

    public boolean isFirstRejectedRequest(String key) {
        Window window = this.rateLimitWindows.get(key);
        if (window == null || Instant.now().isAfter(window.expiresAt())) {
            return false;
        }
        return window.count().get() == (long)this.maxRequests + 1L;
    }

    public long getUnauthorizedCount(String key) {
        Window window = this.unauthorizedWindows.get(key);
        if (window == null || Instant.now().isAfter(window.expiresAt())) {
            return 0L;
        }
        return window.count().get();
    }

    public void resetCounter(String key) {
        this.rateLimitWindows.remove(key);
        this.unauthorizedWindows.remove(key);
    }

    private Window getOrCreateWindow(ConcurrentHashMap<String, Window> map, String key, int ttlSeconds) {
        Window existing = map.get(key);
        if (existing == null || Instant.now().isAfter(existing.expiresAt())) {
            Window fresh = new Window(new AtomicLong(0L), Instant.now().plusSeconds(ttlSeconds));
            Window previous = map.putIfAbsent(key, fresh);
            return previous != null && Instant.now().isBefore(previous.expiresAt()) ? previous : fresh;
        }
        return existing;
    }

    private record Window(AtomicLong count, Instant expiresAt) {
    }
}

