package org.niket.records.membership;

public record CreateMembershipRequest(
        Integer userId,
        Integer channelId,
        Boolean isStarred,
        Boolean isMuted
) {
}
