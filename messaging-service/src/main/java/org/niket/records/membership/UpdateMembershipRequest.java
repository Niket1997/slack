package org.niket.records.membership;

public record UpdateMembershipRequest(
        Boolean isStarred,
        Boolean isMuted,
        Long checkpoint
) {
}
