package com.categoryapi.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisDaoPattern {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 레디스에 값 설정 및 만료시간 설정
     * @param key
     * @param object
     * @param expire
     * @throws Exception
     */
    public void setRedisData(String key,Object object, int expire) throws Exception {
        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(object), expire, TimeUnit.SECONDS);

    }

    /**
     * 레디스에서 값 가져오기
     * json형식으로 받은 후 객체로 리턴
     * @param key
     * @param classType
     * @return
     * @param <T>
     * @throws Exception
     */
    public <T> T getRedisData(String key, Class<T> classType) throws Exception {

        String jsonResult = (String) redisTemplate.opsForValue().get(key);
        if (StringUtil.isNullOrEmpty(jsonResult)) {
            return null;
        } else {
            ObjectMapper mapper = new ObjectMapper();
            T obj = mapper.readValue(jsonResult, classType);
            return obj;
        }
    }

    /**
     * 레디스 키 삭제
     * @param key
     * @throws Exception
     */
    public void delRedisKey(String key) throws Exception {
        redisTemplate.delete(key);
    }
}
