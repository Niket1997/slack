package org.niket.repositories;

import java.util.List;
import org.niket.entities.Message;
import org.springframework.data.repository.CrudRepository;

public interface IMessageRepository extends CrudRepository<Message, Integer> {
  List<Message> findByChannelIdAndSenderUserId(Integer channelId, Integer senderId);

  List<Message> findByChannelId(Integer channelId);

  List<Message> findBySenderUserId(Integer senderUserId);
}
