package org.niket.controllers;

import org.niket.entities.User;
import org.niket.interfaces.IUserService;
import org.niket.records.user.UpsertUserRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public User createUser(@RequestBody UpsertUserRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Integer userId, @RequestBody UpsertUserRequest request) {
        return userService.updateUser(userId, request);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Integer userId) {
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
    }
}
