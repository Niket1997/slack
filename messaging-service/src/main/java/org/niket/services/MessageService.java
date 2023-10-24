package org.niket.services;

import org.jetbrains.annotations.NotNull;
import org.niket.entities.Message;
import org.niket.exceptions.EntityNotFoundException;
import org.niket.interfaces.IChannelService;
import org.niket.interfaces.IMessageService;
import org.niket.interfaces.IUserService;
import org.niket.records.message.CreateMessageRequest;
import org.niket.records.message.UpdateMessageRequest;
import org.niket.repositories.IMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService implements IMessageService {
    private final IMessageRepository messageRepository;
    private final IChannelService channelService;
    private final IUserService userService;

    public MessageService(IMessageRepository messageRepository, IChannelService channelService, IUserService userService) {
        this.messageRepository = messageRepository;
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public Message createMessage(@NotNull CreateMessageRequest request) {
        // If the channel doesn't exist, then following method will throw EntityNotFoundException
        channelService.getChannel(request.channelId());
        // If the user doesn't exist, then following method will throw EntityNotFoundException
        userService.getUser(request.senderUserId());
        Message message = new Message();
        message.setText(request.text());
        message.setSenderUserId(request.senderUserId());
        message.setChannelId(request.channelId());
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessage(Integer messageId, UpdateMessageRequest request) {
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isEmpty()) throw new EntityNotFoundException("no message found with id: " + messageId);
        Message message = messageOptional.get();
        if (request.text() != null && !request.text().isEmpty()) message.setText(request.text());
        message.markUpdated();
        return messageRepository.save(message);
    }

    @Override
    public Message getMessage(Integer messageId) {
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isEmpty()) throw new EntityNotFoundException("no message found with id: " + messageId);
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
        if (messageOptional.isEmpty()) throw new EntityNotFoundException("no message found with id: " + messageId);
        Message message = messageOptional.get();
        message.markUpdated();
        message.markDeleted();
        messageRepository.save(message);
    }
}
