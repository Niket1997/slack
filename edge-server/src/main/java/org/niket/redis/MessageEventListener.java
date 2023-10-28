package org.niket.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.niket.models.MessageEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component("customMessageListener")
public class MessageEventListener implements MessageListener {
    private final ObjectMapper objectMapper;

    private final RedisTemplate<Integer, Object> redisTemplate;

    public MessageEventListener(ObjectMapper objectMapper, RedisTemplate<Integer, Object> redisTemplate) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        try {
            log.info("New message received: {}", message);
            MessageEvent messageEvent = objectMapper.readValue(message.getBody(), MessageEvent.class);
            log.info("Message event is: {}", messageEvent);
            redisTemplate.opsForValue().set(messageEvent.getId(), messageEvent);
        } catch (IOException e) {
            log.error("error while parsing message");
        }
    }
}
