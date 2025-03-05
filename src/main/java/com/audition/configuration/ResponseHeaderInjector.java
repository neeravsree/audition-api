package com.audition.configuration;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@WebFilter("/*")
// TODO Inject openTelemetry trace and span Ids in the response headers.
public class ResponseHeaderInjector extends OncePerRequestFilter {

    private final Tracer tracer;
    private static final String spanIdHeader="X-B3-SpanId";
    private static final String TraceIdHeader="X-B3-TraceId";

    public ResponseHeaderInjector(Tracer tracer) {
        this.tracer = tracer;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        Span currentSpan = tracer.spanBuilder("inject-response-headers").startSpan();
        try {
            // Extract trace context from the current span
            String traceId = currentSpan.getSpanContext().getTraceId();
            String spanId = currentSpan.getSpanContext().getSpanId();

            // Add the trace and span IDs to the response headers
            response.setHeader(spanIdHeader, traceId);
            response.setHeader(TraceIdHeader, spanId);

            // Continue the filter chain
            filterChain.doFilter(request, response);
        } finally {
            currentSpan.end();  // Always end the span when the request completes
        }
    }



}
