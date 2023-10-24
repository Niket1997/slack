package org.niket.repositories;

import org.niket.entities.Membership;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IMembershipRepository extends CrudRepository<Membership, Integer> {
    Optional<Membership> findByUserIdAndChannelId(Integer userId, Integer channelId);

    List<Membership> findByUserId(Integer userId);

    List<Membership> findByChannelId(Integer channelId);
}
