package org.niket.interfaces;

import java.util.List;
import org.niket.entities.Message;
import org.niket.records.message.CreateMessageRequest;
import org.niket.records.message.UpdateMessageRequest;

public interface IMessageService {
  Message createMessage(CreateMessageRequest request);

  Message updateMessage(Integer messageId, UpdateMessageRequest request);

  Message getMessage(Integer messageId);

  List<Message> getMessagesInChannel(Integer channelId);

  List<Message> getMessagesInChannelFromUser(Integer channelId, Integer senderUserId);

  List<Message> getMessagesFromUser(Integer senderUserId);

  void deleteMessage(Integer messageId);
}
