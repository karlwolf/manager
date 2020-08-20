package br.com.arctica.xone.manager.services;

import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;

import br.com.arctica.xone.manager.models.AgentConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AgentService {

    private final SplunkService splunkService;
    private final ReactiveRedisOperations<String, AgentConfig> agentOps;

    public AgentService(ReactiveRedisOperations<String, AgentConfig> agentOps, SplunkService splunkService) {
        this.agentOps = agentOps;
        this.splunkService = splunkService;
    }

    public Mono<AgentConfig> updateConfigFromSplunkByLogin(String login) {
        Flux.fromStream(splunkService.getUserConfigByLogin(login).stream())
                .flatMap(agentMap -> agentOps.opsForValue().set(agentMap.getLogin(), agentMap))
                .thenMany(agentOps.keys("*").flatMap(agentOps.opsForValue()::get)).subscribe();
        return agentOps.opsForValue().get(login);
    }

}