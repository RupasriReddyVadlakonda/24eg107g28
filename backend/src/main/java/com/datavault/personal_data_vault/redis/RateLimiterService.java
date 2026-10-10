package com.datavault.personal_data_vault.redis;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
    @Value("${app.rate-limit.max-requests}")
    private int maxRequests;

    @Value("${app.rate-limit.window-seconds}")
    private int windowSeconds;

    @Value("${app.security-alert.window-seconds}")
    private int alertWindowSeconds;

    private final ConcurrentHashMap<String, Window> rateLimitWindows = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Window> unauthorizedWindows = new ConcurrentHashMap<>();

    public boolean isAllowed(String key) {
        Window window = getOrCreateWindow(rateLimitWindows, key, windowSeconds);
        long count = window.count().incrementAndGet();
        if (count == (long) maxRequests + 1L) {
            window.firstRejectedAlertAvailable().compareAndSet(false, true);
        }
        if (count > maxRequests) {
            return false;
        }
        return true;
    }

    public long incrementUnauthorizedCounter(String key) {
        Window window = getOrCreateWindow(unauthorizedWindows, key, alertWindowSeconds);
        return window.count().incrementAndGet();
    }

    public long getRateLimitCount(String key) {
        Window window = rateLimitWindows.get(key);
        if (window == null || !Instant.now().isBefore(window.expiresAt())) {
            return 0L;
        }
        return window.count().get();
    }

    public boolean isFirstRejectedRequest(String key) {
        Window window = rateLimitWindows.get(key);
        return window != null
                && Instant.now().isBefore(window.expiresAt())
                && window.firstRejectedAlertAvailable().compareAndSet(true, false);
    }

    public long getUnauthorizedCount(String key) {
        Window window = unauthorizedWindows.get(key);
        if (window == null || !Instant.now().isBefore(window.expiresAt())) {
            return 0L;
        }
        return window.count().get();
    }

    public void resetCounter(String key) {
        rateLimitWindows.remove(key);
        unauthorizedWindows.remove(key);
    }

    private Window getOrCreateWindow(ConcurrentHashMap<String, Window> map, String key, int ttlSeconds) {
        Instant now = Instant.now();
        return map.compute(key, (ignored, current) ->
                current == null || !now.isBefore(current.expiresAt())
                        ? new Window(new AtomicLong(), new AtomicBoolean(), now.plusSeconds(ttlSeconds))
                        : current);
    }

    private record Window(AtomicLong count, AtomicBoolean firstRejectedAlertAvailable, Instant expiresAt) {
    }
}
