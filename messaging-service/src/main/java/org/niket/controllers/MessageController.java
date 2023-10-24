package org.niket.controllers;

import org.niket.entities.Message;
import org.niket.exceptions.InvalidRequestException;
import org.niket.interfaces.IMessageService;
import org.niket.records.message.CreateMessageRequest;
import org.niket.records.message.UpdateMessageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/messages")
public class MessageController {
    private final IMessageService messageService;

    public MessageController(IMessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("")
    public Message createMessage(@RequestBody CreateMessageRequest request) {
        if (request.text() == null || request.text().isEmpty() || request.senderUserId() == null || request.channelId() == null) {
            throw new InvalidRequestException("message text, senderUserId & channelId shouldn't be null");
        }
        return messageService.createMessage(request);
    }

    @PutMapping("/{messageId}")
    public Message updateMessage(@PathVariable Integer messageId, @RequestBody UpdateMessageRequest request) {
        if (request.text() == null || request.text().isEmpty()) {
            throw new InvalidRequestException("message text shouldn't be null");
        }
        return messageService.updateMessage(messageId, request);
    }

    @GetMapping("/{messageId}")
    public Message getMessage(@PathVariable Integer messageId) {
        return messageService.getMessage(messageId);
    }

    @GetMapping("/channel/{channelId}")
    public List<Message> getMessagesInChannel(@PathVariable Integer channelId) {
        return messageService.getMessagesInChannel(channelId);
    }

    @GetMapping("")
    public List<Message> getMessagesInChannelFromUser(@RequestParam(name = "channelId") Integer channelId, @RequestParam(name = "senderUserId") Integer senderUserId) {
        return messageService.getMessagesInChannelFromUser(channelId, senderUserId);
    }

    @GetMapping("/user/{userId}")
    public List<Message> getMessagesFromUser(@PathVariable Integer userId) {
        return messageService.getMessagesFromUser(userId);
    }

    @DeleteMapping("/{messageId}")
    public void deleteMessage(@PathVariable Integer messageId) {
        messageService.deleteMessage(messageId);
    }
}
