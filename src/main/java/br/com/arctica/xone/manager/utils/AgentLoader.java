package br.com.arctica.xone.manager.utils;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;

import br.com.arctica.xone.manager.models.AgentConfig;
import br.com.arctica.xone.manager.services.SplunkService;
import reactor.core.publisher.Flux;

@Component
public class AgentLoader {

    private final SplunkService splunkService;
    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, AgentConfig> agentOps;

    public AgentLoader(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, AgentConfig> agentOps,
            SplunkService splunkService) {
        this.factory = factory;
        this.agentOps = agentOps;
        this.splunkService = splunkService;
    }

    @PostConstruct
    public void loadData() {
        factory.getReactiveConnection().serverCommands().flushAll()
                .thenMany(Flux.fromStream(splunkService.getAllUsersConfigs().stream())
                        .flatMap(agentMap -> agentOps.opsForValue().set(agentMap.getLogin(), agentMap)))
                .thenMany(agentOps.keys("*").flatMap(agentOps.opsForValue()::get)).subscribe();
    }
}