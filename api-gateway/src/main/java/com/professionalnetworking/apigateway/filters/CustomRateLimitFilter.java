package com.professionalnetworking.apigateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
public class CustomRateLimitFilter extends AbstractGatewayFilterFactory<CustomRateLimitFilter.Config> {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public CustomRateLimitFilter(ReactiveRedisTemplate<String, String> redisTemplate) {
        super(Config.class);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String key = getKey(exchange);

            return redisTemplate.opsForValue()
                    .increment(key)
                    .flatMap(count -> {
                        if (count == 1) {
                            redisTemplate.expire(key, Duration.ofMinutes(1)).subscribe();
                        }

                        if (count > config.getLimit()) {
                            log.warn("Rate limit exceeded for key: {}, count: {}", key, count);
                            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                            return exchange.getResponse().setComplete();
                        }

                        return chain.filter(exchange);
                    });
        };
    }

    private String getKey(org.springframework.web.server.ServerWebExchange exchange) {
        String userId = exchange.getRequest().getHeaders().getFirst("X-USER-ID");
        if (userId != null) {
            return "rate_limit:user:" + userId;
        }

        String clientIp = exchange.getRequest().getRemoteAddress() != null
                ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                : "unknown";
        return "rate_limit:ip:" + clientIp;
    }

    public static class Config {
        private int limit = 100;

        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }
    }
}
