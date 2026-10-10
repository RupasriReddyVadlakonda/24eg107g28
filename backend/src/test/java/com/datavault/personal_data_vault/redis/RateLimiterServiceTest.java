package com.datavault.personal_data_vault.redis;

import com.datavault.personal_data_vault.redis.RateLimiterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class RateLimiterServiceTest {
    RateLimiterServiceTest() {
    }

    @Test
    void allowsRequestsThroughLimitThenRejectsThem() {
        RateLimiterService limiter = new RateLimiterService();
        ReflectionTestUtils.setField((Object)limiter, (String)"maxRequests", (Object)2);
        ReflectionTestUtils.setField((Object)limiter, (String)"windowSeconds", (Object)60);
        ReflectionTestUtils.setField((Object)limiter, (String)"alertWindowSeconds", (Object)300);
        Assertions.assertTrue((boolean)limiter.isAllowed("client-7"));
        Assertions.assertTrue((boolean)limiter.isAllowed("client-7"));
        Assertions.assertFalse((boolean)limiter.isAllowed("client-7"));
    }

    @Test
    void identifiesOnlyTheFirstRejectedRequestForAlerting() {
        RateLimiterService limiter = new RateLimiterService();
        ReflectionTestUtils.setField((Object)limiter, (String)"maxRequests", (Object)2);
        ReflectionTestUtils.setField((Object)limiter, (String)"windowSeconds", (Object)60);
        ReflectionTestUtils.setField((Object)limiter, (String)"alertWindowSeconds", (Object)300);
        limiter.isAllowed("client-7");
        limiter.isAllowed("client-7");
        limiter.isAllowed("client-7");
        Assertions.assertTrue((boolean)limiter.isFirstRejectedRequest("client-7"));
        limiter.isAllowed("client-7");
        Assertions.assertFalse((boolean)limiter.isFirstRejectedRequest("client-7"));
    }

    @Test
    void startsANewRateLimitWindowAfterExpiry() throws InterruptedException {
        RateLimiterService limiter = new RateLimiterService();
        ReflectionTestUtils.setField(limiter, "maxRequests", 1);
        ReflectionTestUtils.setField(limiter, "windowSeconds", 1);
        ReflectionTestUtils.setField(limiter, "alertWindowSeconds", 1);

        Assertions.assertTrue(limiter.isAllowed("client-expiry"));
        Assertions.assertFalse(limiter.isAllowed("client-expiry"));
        Assertions.assertTrue(limiter.isFirstRejectedRequest("client-expiry"));

        Thread.sleep(1_100L);

        Assertions.assertTrue(limiter.isAllowed("client-expiry"));
        Assertions.assertEquals(1L, limiter.getRateLimitCount("client-expiry"));
    }

    @Test
    void unauthorizedCounterIncrementsAndResets() {
        RateLimiterService limiter = new RateLimiterService();
        ReflectionTestUtils.setField((Object)limiter, (String)"maxRequests", (Object)10);
        ReflectionTestUtils.setField((Object)limiter, (String)"windowSeconds", (Object)60);
        ReflectionTestUtils.setField((Object)limiter, (String)"alertWindowSeconds", (Object)300);
        Assertions.assertEquals((long)1L, (long)limiter.incrementUnauthorizedCounter("app:user"));
        Assertions.assertEquals((long)2L, (long)limiter.incrementUnauthorizedCounter("app:user"));
        limiter.resetCounter("app:user");
        Assertions.assertEquals((long)1L, (long)limiter.incrementUnauthorizedCounter("app:user"));
    }
}

