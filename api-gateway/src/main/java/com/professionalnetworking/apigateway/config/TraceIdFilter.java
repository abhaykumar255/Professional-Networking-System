package com.professionalnetworking.apigateway.config;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

//@RequiredArgsConstructor
//@Component
//public class TraceIdFilter implements WebFilter {
//
//    private final Tracer tracer;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        String traceId = tracer.currentSpan() != null ?
//                tracer.currentSpan().context().traceId() : "no-trace";
//        exchange.getResponse().getHeaders().add("X-Trace-ID", traceId);
//        return chain.filter(exchange);
//    }
//}
