package org.heureum.api.config;

import org.heureum.api.session.infra.InMemoryStreamSessionRepository;
import org.heureum.common.time.SystemTimeProvider;
import org.heureum.common.time.TimeProvider;
import org.heureum.core.repository.session.StreamSessionRepository;
import org.heureum.core.service.session.StreamSessionService;
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
