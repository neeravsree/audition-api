package com.audition.configuration;

import com.audition.constants.AuditionConstants;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class ResponseHeaderInjector implements Filter {

    private final Tracer tracer;

    public ResponseHeaderInjector(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // Extract the current trace context from the request (this is the context passed along the request)
        Context context = Context.current();
        Span currentSpan = Span.fromContext(context);
        if (currentSpan == Span.getInvalid()) {
            currentSpan = tracer.spanBuilder(AuditionConstants.INJECT_RESPONSE_HEADERS).startSpan();
        }
        httpServletResponse.setHeader(AuditionConstants.TRACEID, currentSpan.getSpanContext().getTraceId());
        httpServletResponse.setHeader(AuditionConstants.SPANID, currentSpan.getSpanContext().getSpanId());
        chain.doFilter(request, response);
    }

}