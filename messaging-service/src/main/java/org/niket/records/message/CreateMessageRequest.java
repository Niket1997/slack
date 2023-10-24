package org.niket.records.message;

public record CreateMessageRequest(
        String text,
        Integer senderUserId,
        Integer channelId
) {
}
