package org.niket.redis;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.niket.models.MessageEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component("customMessageListener")
public class MessageEventListener implements MessageListener {
  @Value("${socket.client.event.read-message}")
  private String readMessageEvent;

  private final ObjectMapper objectMapper;

  private final SocketIOServer socketIOServer;

  public MessageEventListener(ObjectMapper objectMapper, SocketIOServer socketIOServer) {
    this.objectMapper = objectMapper;
    this.socketIOServer = socketIOServer;
  }

  @Override
  public void onMessage(@NotNull Message message, byte[] pattern) {
    try {
      MessageEvent messageEvent = objectMapper.readValue(message.getBody(), MessageEvent.class);
      log.info("Message event is: {}", messageEvent);

      Optional<SocketIOClient> senderClient =
          socketIOServer
              .getAllClients()
              .stream()
              .filter(it -> it.get("userId") == messageEvent.getSenderUserId())
              .findFirst();
      if (senderClient.isEmpty()) {
        socketIOServer
            .getRoomOperations(String.valueOf(messageEvent.getChannelId()))
            .sendEvent(readMessageEvent, messageEvent);
      } else {
        socketIOServer
            .getRoomOperations(String.valueOf(messageEvent.getChannelId()))
            .sendEvent(readMessageEvent, senderClient.get(), messageEvent);
      }
    } catch (IOException e) {
      log.error("error while parsing message");
    }
  }
}
