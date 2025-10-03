package com.studyMate.studyMate.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate <String, String> redisTemplate;

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void setValue(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // Sorted Set에 값 추가 (점수 포함)
    public void addToSortedSet(String key, String value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    // Sorted Set에서 점수 순 조회
    public Long getReverseRankFromSortedSet(String key, String value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    // Sorted Set에서 범위 조회 (점수 포함, 내림차순), 시작 인덱스 (0부터)
    public Set<ZSetOperations.TypedTuple<String>> getRangeWithScoresFromSortedSet(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    // Sorted Set 크기 조회
    public Long getSortedSetSize(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }
}
