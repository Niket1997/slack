package org.niket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "memberships")
@Table(indexes = {
        @Index(name = "idx_memberships_user_id", columnList = "userId"), // get all channels for a user
        @Index(name = "idx_memberships_channel_id", columnList = "channelId"),// get all users in the channel
        @Index(name = "idx_memberships_user_id_channel_id", columnList = "userId, channelId", unique = true)
        // get checkpoint for a certain user for a certain channel can be done by querying the membership id
        // if not, we can query the memberships table based on userId & channelId, in which case the index on
        // userId will be used. Here, for a certain userId & channelId, we want a single entry in the memberships
        // table hence adding a unique constraint over (userId, channelId)
})
@Where(clause = "deleted_at IS NULL")
public class Membership extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer channelId;
    private Boolean isStarred;
    private Boolean isMuted;
    private Long checkpoint;
}
