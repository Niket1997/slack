package org.niket.interfaces;

import org.niket.entities.Membership;
import org.niket.records.membership.CreateMembershipRequest;
import org.niket.records.membership.UpdateMembershipRequest;

import java.util.List;

public interface IMembershipService {
    Membership createMembership(CreateMembershipRequest request);

    Membership getMembership(Integer membershipId);

    Membership getMembership(Integer userId, Integer channelId);

    Membership updateMembership(Integer membershipId, UpdateMembershipRequest request);

    List<Membership> getChannelsForUser(Integer userId);

    List<Membership> getUsersInChannel(Integer channelId);

    void deleteMembership(Integer membershipId);
}
