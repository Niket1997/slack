package org.niket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "messages")
@Table(indexes = {
        @Index(name = "idx_messages_channel_id", columnList = "channelId"), // get all messages in channel
        @Index(name = "idx_messages_sender_user_id", columnList = "senderUserId"), // get all messages from a sender user
        @Index(name = "idx_messages_channel_id_sender_user_id", columnList = "channelId, senderUserId"), // get all messages in a channel from a sender user
})
@Where(clause = "deleted_at IS NULL")
@RedisHash("Message")
public class Message extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String text;
    private Integer senderUserId;
    private Integer channelId;
}
