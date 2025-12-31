package org.heureum.api.config;

import org.heureum.api.stream.infra.jpa.StreamJpaRepository;
import org.heureum.api.stream.infra.jpa.StreamRepositoryJpaAdapter;
import org.heureum.core.repository.stream.StreamRepository;
import org.heureum.core.service.stream.StreamService;
import org.heureum.core.service.watch.WatchTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreWiringConfig {

    @Bean
    public StreamRepository streamRepository(StreamJpaRepository jpaRepo) {
        return new StreamRepositoryJpaAdapter(jpaRepo);
    }
    @Bean
    public StreamService streamService(StreamRepository streamRepository) {
        return new StreamService(streamRepository);
    }
    @Bean
    public WatchTokenService watchTokenService(StreamRepository streamRepository) {
        return new WatchTokenService(streamRepository);
    }
}
