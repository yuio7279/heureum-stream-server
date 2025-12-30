package org.heureum.api.config;

import org.heureum.common.time.SystemTimeProvider;
import org.heureum.common.time.TimeProvider;
import org.heureum.core.stream.InMemoryStreamSessionRepository;
import org.heureum.core.stream.StreamSessionRepository;
import org.heureum.core.stream.StreamSessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreStreamConfiguration {

    @Bean
    public TimeProvider timeProvider() {
        return SystemTimeProvider.utc();
    }

    @Bean
    public StreamSessionRepository streamSessionRepository() {
        return new InMemoryStreamSessionRepository();
    }

    @Bean
    public StreamSessionService streamSessionService(
            StreamSessionRepository repository,
            TimeProvider timeProvider
    ) {
        return new StreamSessionService(repository, timeProvider);
    }
}
