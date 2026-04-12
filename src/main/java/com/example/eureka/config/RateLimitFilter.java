package com.example.eureka.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@Order(1)
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);

    private final Cache<String, Bucket> bucketCache = Caffeine.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .maximumSize(10_000)
            .build();

    private Bucket createBucket(String path) {
        if (path.startsWith("/auth/login")) {
            return Bucket.builder()
                    .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1))))
                    .build();
        }
        if (path.startsWith("/auth/register")) {
            return Bucket.builder()
                    .addLimit(Bandwidth.classic(3, Refill.intervally(3, Duration.ofMinutes(1))))
                    .build();
        }
        if (path.startsWith("/mer") || path.startsWith("/invoices")) {
            return Bucket.builder()
                    .addLimit(Bandwidth.classic(60, Refill.intervally(60, Duration.ofMinutes(1))))
                    .build();
        }
        return Bucket.builder()
                .addLimit(Bandwidth.classic(200, Refill.intervally(200, Duration.ofMinutes(1))))
                .build();
    }

    private String getBucketKey(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        return ip + ":" + request.getRequestURI();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String key = getBucketKey(request);
        Bucket bucket = bucketCache.get(key, k -> createBucket(request.getRequestURI()));

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("Rate limit prekoračen — ip: {}, path: {}", request.getRemoteAddr(), request.getRequestURI());
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":429,\"message\":\"Previše zahtjeva, pokušajte kasnije\"}");
        }
    }
}
