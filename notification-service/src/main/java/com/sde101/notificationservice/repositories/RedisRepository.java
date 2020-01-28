package com.sde101.notificationservice.repositories;

import com.sde101.notificationservice.models.responses.IsBlaklistedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.Set;

@Repository
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class RedisRepository {

    private final Logger logger = LoggerFactory.getLogger(RedisRepository.class);
    private static final String KEY = "Blacklisted";

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> opsForSet;

    public void insertInBlackListed(String... numbers) {
        Long response = opsForSet.add(KEY, numbers);
        logger.info(response.toString());
    }


    @Cacheable(value = "mycache", key = "#number")
    public IsBlaklistedResponse ifNumberBlackListed(String number) {
        logger.info("redisrepository called");
        if (opsForSet.isMember(KEY, number) == true) {
            return new IsBlaklistedResponse(number, true);
        } else {
            return new IsBlaklistedResponse(number, false);
        }
    }

    @CacheEvict(value = "mycache", key = "#number")
    public void removeFromBlacklist(String number) throws EntityNotFoundException {

        Long response = opsForSet.remove(KEY, number);
        if (response == 0) {
            throw new EntityNotFoundException();
        }
    }

    public Set<String> getAllBlackListedNumbers() {
        return opsForSet.members(KEY);
    }


}