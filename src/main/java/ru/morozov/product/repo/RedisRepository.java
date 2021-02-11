package ru.morozov.product.repo;

public interface RedisRepository {
    void add(String key, Object value);
    Object find(String key);
}