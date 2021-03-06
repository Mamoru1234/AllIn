package com.github.allin.services;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 */
@Log4j
@Service
public class GrantTokenServiceImpl implements GrantTokenService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Override
    public String getToken(String userID) {
        String grantToken = UUID.randomUUID().toString();
        log.debug("grantToken: " + grantToken);
        valueOperations.set(grantToken, userID, 1, TimeUnit.DAYS);
        return grantToken;
    }

    @Override
    public String getUserID(String grantToken) {
        return valueOperations.get(grantToken);
    }

    @Override
    public void invalidate(String grantToken) {
        valueOperations.set(grantToken, null, 1, TimeUnit.MILLISECONDS);
    }
}
