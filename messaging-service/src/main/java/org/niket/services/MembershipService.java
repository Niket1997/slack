package org.niket.services;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.niket.entities.Membership;
import org.niket.exceptions.EntityNotFoundException;
import org.niket.interfaces.IChannelService;
import org.niket.interfaces.IMembershipService;
import org.niket.interfaces.IUserService;
import org.niket.records.membership.CreateMembershipRequest;
import org.niket.records.membership.UpdateMembershipRequest;
import org.niket.repositories.IMembershipRepository;
import org.springframework.stereotype.Service;

@Service
public class MembershipService implements IMembershipService {

  private final IMembershipRepository membershipRepository;

  private final IUserService userService;
  private final IChannelService channelService;

  public MembershipService(
      IMembershipRepository membershipRepository,
      IUserService userService,
      IChannelService channelService) {
    this.membershipRepository = membershipRepository;
    this.userService = userService;
    this.channelService = channelService;
  }

  @Override
  public Membership createMembership(@NotNull CreateMembershipRequest request) {
    // If the user doesn't exist, then following method will throw EntityNotFoundException
    userService.getUser(request.userId());
    // If the channel doesn't exist, then following method will throw EntityNotFoundException
    channelService.getChannel(request.channelId());

    Membership membership = new Membership();
    membership.setUserId(request.userId());
    membership.setChannelId(request.channelId());
    if (request.isStarred() != null) membership.setIsStarred(request.isStarred());
    if (request.isMuted() != null) membership.setIsMuted(request.isMuted());
    return membershipRepository.save(membership);
  }

  @Override
  public Membership getMembership(Integer membershipId) {
    Optional<Membership> membershipOptional = membershipRepository.findById(membershipId);
    if (membershipOptional.isEmpty())
      throw new EntityNotFoundException(
          "no membership found for given membership id: " + membershipId);
    return membershipOptional.get();
  }

  @Override
  public Membership getMembership(Integer userId, Integer channelId) {
    Optional<Membership> membershipOptional =
        membershipRepository.findByUserIdAndChannelId(userId, channelId);
    if (membershipOptional.isEmpty())
      throw new EntityNotFoundException(
          "no membership found for given user id: " + userId + " & channel id: " + channelId);
    return membershipOptional.get();
  }

  @Override
  public Membership updateMembership(Integer membershipId, UpdateMembershipRequest request) {
    Optional<Membership> membershipOptional = membershipRepository.findById(membershipId);
    if (membershipOptional.isEmpty())
      throw new EntityNotFoundException(
          "no membership found for given membership id: " + membershipId);
    Membership membership = membershipOptional.get();
    if (request.isStarred() != null) membership.setIsStarred(request.isStarred());
    if (request.isMuted() != null) membership.setIsMuted(request.isMuted());
    if (request.checkpoint() != null) membership.setCheckpoint(request.checkpoint());
    membership.markUpdated();
    return membershipRepository.save(membership);
  }

  @Override
  public List<Membership> getChannelsForUser(Integer userId) {
    return membershipRepository.findByUserId(userId);
  }

  @Override
  public List<Membership> getUsersInChannel(Integer channelId) {
    return membershipRepository.findByChannelId(channelId);
  }

  @Override
  public void deleteMembership(Integer membershipId) {
    Optional<Membership> membershipOptional = membershipRepository.findById(membershipId);
    if (membershipOptional.isEmpty())
      throw new EntityNotFoundException("no membership found with id: " + membershipId);
    Membership membership = membershipOptional.get();
    membership.markUpdated();
    membership.markDeleted();
    membershipRepository.save(membership);
  }
}
