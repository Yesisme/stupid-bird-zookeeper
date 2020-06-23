package com.lym.zk.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.lym.zk.utils.DistributedRedisLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class RedisConfig {

    @Bean(name = "redisTemplate")
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer =
                new FastJsonRedisSerializer<>(Object.class);
        setSerialize(redisTemplate, fastJsonRedisSerializer);
        return redisTemplate;
    }

    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer =
                new FastJsonRedisSerializer<>(Object.class);
        setSerialize(redisTemplate, fastJsonRedisSerializer);
        return redisTemplate;
    }

    @Bean(name = "jedisPoolConfig")
    @Conditional(JedisCondition.class)
    @ConditionalOnMissingBean(value = JedisPoolConfig.class)
    public JedisPoolConfig jedisPoolConfig(RedisProperties redisProperties) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getJedis().getPool().getMaxWait().getSeconds());
        jedisPoolConfig.setMaxTotal(redisProperties.getJedis().getPool().getMaxActive());
        jedisPoolConfig.setMinIdle(redisProperties.getJedis().getPool().getMinIdle());
        return jedisPoolConfig;
    }

    @Bean(name = "jedisPool")
    @Conditional(JedisCondition.class)
    @ConditionalOnMissingBean(value = JedisPool.class)
    @ConditionalOnProperty(prefix = "spring.redis", name = "enable-sentinel",
            matchIfMissing = true, havingValue = "false")
    public JedisPool jedisPool(RedisProperties redisProperties, JedisPoolConfig jedisPoolConfig) {

        return new JedisPool(jedisPoolConfig, redisProperties.getHost(), redisProperties.getPort(),
                redisProperties.getTimeout().getNano(), redisProperties.getPassword(), redisProperties.getDatabase());

    }

    @Bean(name = "redissonClient")
    @Conditional(RedissionCondition.class)
    @ConditionalOnProperty(prefix = "spring.redis", name = "enable-sentinel",
            matchIfMissing = true, havingValue = "false")
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(
                "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setDatabase(redisProperties.getDatabase())
                .setConnectTimeout(redisProperties.getTimeout().getNano() == 0 ? 3000 : redisProperties.getTimeout().getNano());
        if (StringUtils.isNotBlank(redisProperties.getPassword())) {
            singleServerConfig.setPassword(redisProperties.getPassword());
        }
        RedissonClient client = Redisson.create(config);
        DistributedRedisLock.setRedissonClient(client);
        return client;

    }

    @Bean(name = "redissonSentinelClient")
    @Conditional(RedissionCondition.class)
    @ConditionalOnProperty(prefix = "spring.redis", name = "enable-sentinel", havingValue = "true")
    public RedissonClient redissonSentinelClient(RedisProperties redisProperties) {
        Config config = new Config();
        SentinelServersConfig sentinelServersConfig = config.
                useSentinelServers();

        List<String> node = redisProperties.getSentinel().getNodes();

        String[] nodes = redisProperties.getSentinel().getNodes().toArray(new String[0]);

        List<String> newNodes = new ArrayList<>(nodes.length);

        Arrays.stream(nodes).forEach((index) -> newNodes.add(
                index.startsWith("redis://") ? index : "redis://" + index));

        SentinelServersConfig serverConfig = config.useSentinelServers()
                .addSentinelAddress(newNodes.toArray(new String[0]))
                .setMasterName(redisProperties.getSentinel().getMaster())
                .setTimeout((int)redisProperties.getTimeout().getSeconds());
        if (StringUtils.isNotBlank(redisProperties.getPassword())) {
            serverConfig.setPassword(redisProperties.getPassword());
        }
        RedissonClient client = Redisson.create(config);
        DistributedRedisLock.setRedissonClient(client);
        return client;
    }

    @Bean(name = "jedisSentinelPool")
    @Conditional(JedisCondition.class)
    @ConditionalOnMissingBean(value = JedisSentinelPool.class)
    @ConditionalOnProperty(prefix = "spring.redis", name = "enable-sentinel", havingValue = "true")
    public JedisSentinelPool jedisSentinelPool(RedisProperties redisProperties, JedisPoolConfig jedisPoolConfig) {
        Set<String> sentinels =
                redisProperties.getSentinel().getNodes()
                        .stream().filter(node -> StringUtils.isNotBlank(node)).collect(Collectors.toSet());
        JedisSentinelPool jedisSentinelPool =
                new JedisSentinelPool(redisProperties.getSentinel().getMaster(), sentinels,
                        jedisPoolConfig, (int) redisProperties.getTimeout().getSeconds(), redisProperties.getPassword(),
                        redisProperties.getDatabase());
        return jedisSentinelPool;
    }


    private void setSerialize(RedisTemplate redisTemplate, RedisSerializer redisSerializer) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setDefaultSerializer(redisSerializer);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
    }

    static final class RedissionCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata a) {
            return StringUtils.equalsIgnoreCase(context.getEnvironment()
                    .getProperty("spring.redis.enable-redisson"), "true");
        }
    }

    static final class JedisCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata a) {
            return StringUtils.equalsIgnoreCase(context.getEnvironment()
                    .getProperty("spring.redis.enable-jedis"), "true");
        }
    }

}

