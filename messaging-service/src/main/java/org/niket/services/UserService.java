package org.niket.services;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.niket.entities.User;
import org.niket.exceptions.EntityNotFoundException;
import org.niket.interfaces.IUserService;
import org.niket.records.user.UpsertUserRequest;
import org.niket.repositories.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
  private final IUserRepository userRepository;

  public UserService(IUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User createUser(@NotNull UpsertUserRequest request) {
    User user = new User();
    user.setName(request.name());
    user.setEmailId(request.emailId());
    user.setBio(request.bio());
    return userRepository.save(user);
  }

  @Override
  public User getUser(Integer userId) {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty())
      throw new EntityNotFoundException("no user found for given user id: " + userId);
    return userOptional.get();
  }

  @Override
  public User updateUser(Integer userId, UpsertUserRequest request) {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty())
      throw new EntityNotFoundException("no user found for given user id: " + userId);
    User user = userOptional.get();
    if (request.name() != null) user.setName(request.name());
    if (request.emailId() != null) user.setEmailId(request.emailId());
    if (request.bio() != null) user.setBio(request.bio());
    user.markUpdated();
    return userRepository.save(user);
  }

  @Override
  public void deleteUser(Integer userId) {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty())
      throw new EntityNotFoundException("no user found for given user id: " + userId);
    User user = userOptional.get();
    user.markUpdated();
    user.markDeleted();
    userRepository.save(user);
  }
}
