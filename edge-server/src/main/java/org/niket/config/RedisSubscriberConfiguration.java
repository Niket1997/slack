package org.niket.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisSubscriberConfiguration {
    private final MessageListener messageListener;
    private final RedisConnectionFactory redisConnectionFactory;

    public RedisSubscriberConfiguration(@Qualifier("customMessageListener") MessageListener messageListener, RedisConnectionFactory redisConnectionFactory) {
        this.messageListener = messageListener;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(messageListener);
    }


    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(messageListener, new ChannelTopic("1"));
        return redisMessageListenerContainer;
    }
}
