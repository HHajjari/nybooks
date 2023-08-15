package com.ing.nybooks.config.timer;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimerConfig {
    @Bean
    public Timer openLibraryApiCallTimer(MeterRegistry meterRegistry) {
        return Timer.builder("openLibrary.api.call.duration")
                .description("Duration of OpenLibrary API calls")
                .register(meterRegistry);
    }

    @Bean
    public Timer nytBookApiCallTimer(MeterRegistry meterRegistry) {
        return Timer.builder("nytBook.api.call.duration")
                .description("Duration of NYT Book API calls")
                .register(meterRegistry);
    }
}
