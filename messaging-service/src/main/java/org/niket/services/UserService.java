package org.niket.services;

import org.niket.entities.User;
import org.niket.exceptions.UserNotFoundException;
import org.niket.interfaces.IUserService;
import org.niket.records.user.UpsertUserRequest;
import org.niket.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UpsertUserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmailId(request.emailId());
        user.setBio(request.bio());
        return userRepository.save(user);
    }

    @Override
    public User getUser(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) throw new UserNotFoundException("no user found for given user id: " + userId);
        return userOptional.get();
    }

    @Override
    public User updateUser(Integer userId, UpsertUserRequest request) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) throw new UserNotFoundException("no user found for given user id: " + userId);
        User user = userOptional.get();
        user.setName(request.name());
        user.setEmailId(request.emailId());
        user.setBio(request.bio());
        user.markUpdated();
        return userRepository.save(user);
    }
}
