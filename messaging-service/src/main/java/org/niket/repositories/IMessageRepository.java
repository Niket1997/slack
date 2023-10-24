package org.niket.repositories;

import org.niket.entities.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IMessageRepository extends CrudRepository<Message, Integer> {
    List<Message> findByChannelIdAndSenderUserId(Integer channelId, Integer senderId);

    List<Message> findByChannelId(Integer channelId);

    List<Message> findBySenderUserId(Integer senderUserId);

}
