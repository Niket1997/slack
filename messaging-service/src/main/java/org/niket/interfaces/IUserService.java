package org.niket.interfaces;

import org.niket.entities.User;
import org.niket.records.user.UpsertUserRequest;

public interface IUserService {
    User createUser(UpsertUserRequest request);

    User getUser(Integer userId);

    User updateUser(Integer userId, UpsertUserRequest request);
}
