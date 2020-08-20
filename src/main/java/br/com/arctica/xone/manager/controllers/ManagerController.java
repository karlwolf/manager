package br.com.arctica.xone.manager.controllers;

import java.util.List;

import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arctica.xone.manager.models.AgentConfig;
import br.com.arctica.xone.manager.services.AgentService;
import br.com.arctica.xone.manager.services.SplunkService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ManagerController {

	private final ReactiveRedisOperations<String, AgentConfig> agentOps;
	private final AgentService agentService;
	private final SplunkService splunkService;

	ManagerController(ReactiveRedisOperations<String, AgentConfig> agentOps, AgentService agentService, SplunkService splunkService) {
		this.agentOps = agentOps;
		this.agentService = agentService;
		this.splunkService = splunkService;
	}

	@GetMapping("/configs")
	public Flux<AgentConfig> all() {
		return agentOps.keys("*").flatMap(agentOps.opsForValue()::get);
	}

	@GetMapping("/configs/{id}")
	public Mono<AgentConfig> getConfigById(@PathVariable String id) {
		return agentOps.opsForValue().get(id);
	}	

	@GetMapping("/configs/from/splunk")
	public List<AgentConfig> getConfigsFromSplunk() {
		return splunkService.getAllUsersConfigs();
	}

	@GetMapping("/configs/from/splunk/{id}")
	public List<AgentConfig> getConfigFromSplunkById(@PathVariable String id) {
		return splunkService.getUserConfigByLogin(id);
	}	

	@PutMapping("/configs/from/splunk/{id}")
	public Mono<AgentConfig> updateConfigFromSplunkById(@PathVariable String id) {
		return agentService.updateConfigFromSplunkByLogin(id);
	}		
}