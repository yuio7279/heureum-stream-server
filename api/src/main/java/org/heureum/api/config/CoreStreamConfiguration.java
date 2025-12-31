package org.heureum.api.config;

import org.heureum.api.session.infra.jpa.StreamSessionJpaRepository;
import org.heureum.api.session.infra.jpa.StreamSessionRepositoryJpaAdapter;
import org.heureum.api.session.infra.redis.ViewerSessionRedisRepository;
import org.heureum.common.time.SystemTimeProvider;
import org.heureum.common.time.TimeProvider;
import org.heureum.core.repository.session.StreamSessionRepository;
import org.heureum.core.repository.session.ViewerSessionRepository;
import org.heureum.core.service.session.StreamSessionService;
import org.heureum.core.service.session.ViewerSessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class CoreStreamConfiguration {

    @Bean
    public TimeProvider timeProvider() {
        return SystemTimeProvider.utc();
    }

    @Bean
    public StreamSessionRepository streamSessionRepository(StreamSessionJpaRepository jpaRepository) {
        return new StreamSessionRepositoryJpaAdapter(jpaRepository);
    }

    @Bean
    public StreamSessionService streamSessionService(
            StreamSessionRepository repository,
            TimeProvider timeProvider
    ) {
        return new StreamSessionService(repository, timeProvider);
    }

    @Bean
    public ViewerSessionRepository viewerSessionRepository(StringRedisTemplate redisTemplate) {
        return new ViewerSessionRedisRepository(redisTemplate);
    }

    @Bean
    public ViewerSessionService viewerSessionService(
            ViewerSessionRepository repository,
            TimeProvider timeProvider
    ) {
        return new ViewerSessionService(repository, timeProvider);
    }
}
