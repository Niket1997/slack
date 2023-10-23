package org.niket.records.user;

public record UpsertUserRequest(
        String name,
        String emailId,
        String bio
) {
}
