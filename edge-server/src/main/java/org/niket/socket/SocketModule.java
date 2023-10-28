package org.niket.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.niket.exceptions.InvalidRequestException;
import org.niket.redis.MessageEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class SocketModule {
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    @Qualifier("customMessageListener")
    private final MessageEventListener messageListener;

    public SocketModule(SocketIOServer socketIOServer, RedisMessageListenerContainer redisMessageListenerContainer, MessageEventListener messageListener) {
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.messageListener = messageListener;
        socketIOServer.addConnectListener(onConnected());
        socketIOServer.addDisconnectListener(onDisconnected());
    }


    private ConnectListener onConnected() {
        return client -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            if (!params.containsKey("userId") || params.get("userId").size() != 1) {
                throw new InvalidRequestException("userId should be present");
            }

            int userId = Integer.parseInt(params.get("userId").get(0));
            // get channels a user is part of
            Set<String> channels = Set.of("1", "2");
            Set<ChannelTopic> channelTopics = Set.of(new ChannelTopic("1"), new ChannelTopic("2"));
            client.set("userId", userId);
            client.joinRooms(channels);
            redisMessageListenerContainer.addMessageListener(messageListener, channelTopics);
            log.info("## client connected.");
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("** client disconnected.");
        };
    }
}
