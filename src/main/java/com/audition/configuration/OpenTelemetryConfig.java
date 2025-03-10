package com.audition.configuration;

import com.audition.constants.AuditionConstants;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.exporter.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenTelemetryConfig {

    private JaegerGrpcSpanExporter jaegerExporter;
    private SdkTracerProvider tracerProvider;
    @Bean
    public OpenTelemetry openTelemetry() {
        // Jaeger exporter configuration
         jaegerExporter = JaegerGrpcSpanExporter.builder()
            .setEndpoint(AuditionConstants.JAEGER_URL)  // Replace with your Jaeger endpoint
            .build();

         tracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(SimpleSpanProcessor.create(jaegerExporter))
            .build();

        return OpenTelemetrySdk.builder()
            .setTracerProvider(tracerProvider)
            .buildAndRegisterGlobal();
    }

    @Bean
    public Tracer tracer() {
        return openTelemetry().getTracer("audition-api", "1.0");
    }

    @PreDestroy
    public void cleanup() {
        if (jaegerExporter != null) {
            jaegerExporter.close();  // Close the exporter when the application shuts down
        }
        if (tracerProvider != null) {
            tracerProvider.close();  // Close the tracer provider to release resources
        }
    }
}
