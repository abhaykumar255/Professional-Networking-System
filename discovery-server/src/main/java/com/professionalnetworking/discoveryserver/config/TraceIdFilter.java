package com.professionalnetworking.discoveryserver.config;

import io.micrometer.tracing.Tracer;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class TraceIdFilter implements Filter {

    private final Tracer tracer;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (response instanceof HttpServletResponse httpResponse) {
            String traceId = tracer.currentSpan() != null ?
                    tracer.currentSpan().context().traceId() : "no-trace";
            httpResponse.setHeader("X-Trace-ID", traceId);
        }

        chain.doFilter(request, response);
    }
}
