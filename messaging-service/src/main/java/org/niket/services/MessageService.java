package org.niket.services;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.niket.entities.Membership;
import org.niket.entities.Message;
import org.niket.exceptions.EntityNotFoundException;
import org.niket.interfaces.IChannelService;
import org.niket.interfaces.IMembershipService;
import org.niket.interfaces.IMessageService;
import org.niket.interfaces.IUserService;
import org.niket.records.message.CreateMessageRequest;
import org.niket.records.message.UpdateMessageRequest;
import org.niket.redis.MessageEvent;
import org.niket.repositories.IMessageRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageService implements IMessageService {
  private final IMessageRepository messageRepository;
  private final IChannelService channelService;
  private final IUserService userService;
  private final IMembershipService membershipService;

  @Qualifier("customRedisTemplate")
  private final RedisTemplate<Integer, Object> redisTemplate;

  public MessageService(
      IMessageRepository messageRepository,
      IChannelService channelService,
      IUserService userService,
      IMembershipService membershipService,
      RedisTemplate<Integer, Object> redisTemplate) {
    this.messageRepository = messageRepository;
    this.channelService = channelService;
    this.userService = userService;
    this.membershipService = membershipService;
    this.redisTemplate = redisTemplate;
  }

  @Override
  public Message createMessage(@NotNull CreateMessageRequest request) {
    // If the channel doesn't exist, then following method will throw EntityNotFoundException
    channelService.getChannel(request.channelId());
    // If the user doesn't exist, then following method will throw EntityNotFoundException
    userService.getUser(request.senderUserId());
    // validate if a user is part of channel
    // if not then, this will throw error
    Membership membership =
        membershipService.getMembership(request.senderUserId(), request.channelId());

    Message message = new Message();
    message.setText(request.text());
    message.setSenderUserId(request.senderUserId());
    message.setChannelId(request.channelId());
    message = messageRepository.save(message);
    createAndSendMessageEventToRedisTopic(message);
    return message;
  }

  @Override
  public Message updateMessage(Integer messageId, UpdateMessageRequest request) {
    Optional<Message> messageOptional = messageRepository.findById(messageId);
    if (messageOptional.isEmpty())
      throw new EntityNotFoundException("no message found with id: " + messageId);
    Message message = messageOptional.get();
    if (request.text() != null && !request.text().isEmpty()) message.setText(request.text());
    message.markUpdated();
    return messageRepository.save(message);
  }

  @Override
  public Message getMessage(Integer messageId) {
    Optional<Message> messageOptional = messageRepository.findById(messageId);
    if (messageOptional.isEmpty())
      throw new EntityNotFoundException("no message found with id: " + messageId);
    return messageOptional.get();
  }

  @Override
  public List<Message> getMessagesInChannel(Integer channelId) {
    // If the channel doesn't exist, then following method will throw EntityNotFoundException
    channelService.getChannel(channelId);
    return messageRepository.findByChannelId(channelId);
  }

  @Override
  public List<Message> getMessagesInChannelFromUser(Integer channelId, Integer senderUserId) {
    // If the channel doesn't exist, then following method will throw EntityNotFoundException
    channelService.getChannel(channelId);
    // If the user doesn't exist, then following method will throw EntityNotFoundException
    userService.getUser(senderUserId);
    return messageRepository.findByChannelIdAndSenderUserId(channelId, senderUserId);
  }

  @Override
  public List<Message> getMessagesFromUser(Integer senderUserId) {
    // If the user doesn't exist, then following method will throw EntityNotFoundException
    userService.getUser(senderUserId);
    return messageRepository.findBySenderUserId(senderUserId);
  }

  @Override
  public void deleteMessage(Integer messageId) {
    Optional<Message> messageOptional = messageRepository.findById(messageId);
    if (messageOptional.isEmpty())
      throw new EntityNotFoundException("no message found with id: " + messageId);
    Message message = messageOptional.get();
    message.markUpdated();
    message.markDeleted();
    messageRepository.save(message);
  }

  private void createAndSendMessageEventToRedisTopic(Message message) {
    MessageEvent messageEvent = new MessageEvent();
    messageEvent.setId(message.getId());
    messageEvent.setText(message.getText());
    messageEvent.setSenderUserId(message.getSenderUserId());
    messageEvent.setChannelId(message.getChannelId());
    messageEvent.setCreatedAt(message.getCreatedAt());
    messageEvent.setUpdatedAt(message.getUpdatedAt());
    messageEvent.setDeletedAt(message.getDeletedAt());

    log.info("sending message event: {}", messageEvent);
    redisTemplate.convertAndSend(String.valueOf(messageEvent.getChannelId()), messageEvent);
  }
}
