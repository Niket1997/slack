package org.niket.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.niket.exceptions.InvalidRequestException;
import org.niket.external.messaging.MessagingServiceClient;
import org.niket.models.Membership;
import org.niket.redis.MessageEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketModule {
  private final RedisMessageListenerContainer redisMessageListenerContainer;
  private final MessagingServiceClient messagingServiceClient;

  @Qualifier("customMessageListener")
  private final MessageEventListener messageListener;

  public SocketModule(
      SocketIOServer socketIOServer,
      RedisMessageListenerContainer redisMessageListenerContainer,
      MessagingServiceClient messagingServiceClient,
      MessageEventListener messageListener) {
    this.redisMessageListenerContainer = redisMessageListenerContainer;
    this.messagingServiceClient = messagingServiceClient;
    this.messageListener = messageListener;
    socketIOServer.addConnectListener(onConnected());
    socketIOServer.addDisconnectListener(onDisconnected());
  }

  private ConnectListener onConnected() {
    return client -> {
      Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
      if (!params.containsKey("userId") || params.get("userId").size() != 1) {
        client.disconnect();
        throw new InvalidRequestException("userId should be present");
      }

      // get channels a user is part of
      int userId;
      List<Membership> userChannels;
      try {
        userId = Integer.parseInt(params.get("userId").get(0));
        userChannels = messagingServiceClient.getChannelsForUser(userId);
      } catch (Exception e) {
        client.disconnect();
        log.error("error occurred while fetching channels for user, {}", e.getMessage());
        throw e;
      }

      log.info("user channels {}", userChannels);

      Set<String> channels =
          userChannels
              .stream()
              .map(it -> String.valueOf(it.getChannelId()))
              .collect(Collectors.toSet());
      Set<ChannelTopic> channelTopics =
          userChannels
              .stream()
              .map(it -> new ChannelTopic(String.valueOf(it.getChannelId())))
              .collect(Collectors.toSet());

      client.set("userId", userId);
      if (!channels.isEmpty()) client.joinRooms(channels);
      if (!channelTopics.isEmpty())
        redisMessageListenerContainer.addMessageListener(messageListener, channelTopics);
      log.info("## client with userId {} connected. ##", userId);
    };
  }

  private DisconnectListener onDisconnected() {
    return client -> {
      Integer userId = client.get("userId");
      log.info("** client with userId {} disconnected. **", userId);
    };
  }
}
