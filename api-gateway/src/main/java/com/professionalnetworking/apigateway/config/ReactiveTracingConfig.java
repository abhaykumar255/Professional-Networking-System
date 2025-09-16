package com.professionalnetworking.apigateway.config;

import io.micrometer.tracing.Tracer;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Configuration
public class ReactiveTracingConfig {

    @Bean
    public WebFilter mdcWebFilter(Tracer tracer) {
        return new WebFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                return chain.filter(exchange)
                        .doOnEach(signal -> {
                            if (tracer.currentSpan() != null) {
                                MDC.put("traceId", tracer.currentSpan().context().traceId());
                                MDC.put("spanId", tracer.currentSpan().context().spanId());
                            }
                        })
                        .doFinally(signalType -> MDC.clear());
            }

            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }
        };
    }
}
