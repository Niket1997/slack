package org.niket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "messages")
@Table(indexes = {
        @Index(name = "idx_messages_channel_id", columnList = "channelId"), // get all messages in channel
        @Index(name = "idx_messages_sender_user_id", columnList = "senderUserId"), // get all messages from a sender user
        @Index(name = "idx_messages_channel_id_sender_user_id", columnList = "channelId, senderUserId"), // get all messages in a channel from a sender user
})
public class Message extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String text;
    private Integer senderUserId;
    private Integer channelId;
}
