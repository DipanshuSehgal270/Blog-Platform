package com.blog.platform.blog.platform.controller;

import com.blog.platform.blog.platform.dto.RegisterRequest;
import com.blog.platform.blog.platform.dto.UpdateUserRequest;
import com.blog.platform.blog.platform.dto.UserResponse;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterRequest request) {
        UserResponse savedUser = userService.saveUserWithDto(request);
        return ResponseEntity.ok(savedUser);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.mapToUserResponse(userService.getUserByID(id)));
    }

    // Get user by username
    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.mapToUserResponse(userService.getUserByUsername(username)));
    }

    // Get user by email
    @GetMapping("/by-email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.mapToUserResponse(userService.getUserByEmail(email)));
    }

    // Update user profile (e.g., by current username)
    @PutMapping("/update/{currentUsername}")
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable String currentUsername,
            @RequestBody UpdateUserRequest request) {
        User updatedUser = userService.updateUserProfile(currentUsername, request);
        return ResponseEntity.ok(userService.mapToUserResponse(updatedUser));
    }

    // Soft delete by ID
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User soft-deleted.");
    }

    // Hard delete by ID
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<?> hardDeleteUser(@PathVariable Long id) {
        userService.hardDeleteById(id);
        return ResponseEntity.ok("User permanently deleted.");
    }

    // Soft delete by any identifier (username, email, id)
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserByType(
            @RequestParam String type,
            @RequestParam String value
    ) {
        userService.deleteUser(type, value);
        return ResponseEntity.ok("User soft-deleted by " + type + ": " + value);
    }

    // Get all users (admin only?)
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> responses = users.stream()
                .map(userService::mapToUserResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // Promote to admin
    @PostMapping("/promote/{username}")
    public ResponseEntity<?> promoteToAdmin(@PathVariable String username) {
        userService.promoteToAdmin(username);
        return ResponseEntity.ok(username + " has been promoted to ADMIN.");
    }
}
