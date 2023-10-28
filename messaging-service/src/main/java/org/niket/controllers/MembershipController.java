package org.niket.controllers;

import java.util.List;
import org.niket.entities.Membership;
import org.niket.exceptions.InvalidRequestException;
import org.niket.interfaces.IMembershipService;
import org.niket.records.membership.CreateMembershipRequest;
import org.niket.records.membership.UpdateMembershipRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/memberships")
public class MembershipController {
  private final IMembershipService membershipService;

  public MembershipController(IMembershipService membershipService) {
    this.membershipService = membershipService;
  }

  @PostMapping("")
  public Membership createMembership(@RequestBody CreateMembershipRequest request) {
    if (request.userId() == null || request.channelId() == null) {
      throw new InvalidRequestException("userId and channelId shouldn't be null");
    }

    return membershipService.createMembership(request);
  }

  @GetMapping("/{membershipId}")
  public Membership getMembership(@PathVariable Integer membershipId) {
    return membershipService.getMembership(membershipId);
  }

  @GetMapping("")
  public Membership getMembership(
      @RequestParam(name = "userId") Integer userId,
      @RequestParam(name = "channelId") Integer channelId) {
    return membershipService.getMembership(userId, channelId);
  }

  @PutMapping("/{membershipId}")
  public Membership updateMembership(
      @PathVariable Integer membershipId, @RequestBody UpdateMembershipRequest request) {
    return membershipService.updateMembership(membershipId, request);
  }

  @GetMapping("/channels/{userId}")
  public List<Membership> getChannelsForUser(@PathVariable Integer userId) {
    return membershipService.getChannelsForUser(userId);
  }

  @GetMapping("/users/{channelId}")
  public List<Membership> getUsersInChannel(@PathVariable Integer channelId) {
    return membershipService.getUsersInChannel(channelId);
  }

  @DeleteMapping("/{membershipId}")
  public void deleteMembership(@PathVariable Integer membershipId) {
    membershipService.deleteMembership(membershipId);
  }
}
