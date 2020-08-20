package br.com.arctica.xone.manager.configs;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import br.com.arctica.xone.manager.models.AgentConfig;

@Configuration
public class RedisConfiguration {  
    
    @Bean
    ReactiveRedisOperations<String, AgentConfig> redisOperations(ReactiveRedisConnectionFactory factory) {
      Jackson2JsonRedisSerializer<AgentConfig> serializer = new Jackson2JsonRedisSerializer<>(AgentConfig.class);
  
      RedisSerializationContext.RedisSerializationContextBuilder<String, AgentConfig> builder =
          RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
  
      RedisSerializationContext<String, AgentConfig> context = builder.value(serializer).build();
  
      return new ReactiveRedisTemplate<>(factory, context);
    }

}