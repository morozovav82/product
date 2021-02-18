package ru.morozov.product.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    private static final String HASH_KEY = "main";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void add(String key, Object value) {
        hashOperations.put(key, HASH_KEY, value);
        redisTemplate.expire(key, 30, TimeUnit.SECONDS);
    }

    @Override
    public Object find(String key) {
        return hashOperations.get(key, HASH_KEY);
    }
}
