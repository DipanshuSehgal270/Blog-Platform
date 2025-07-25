package com.blog.platform.blog.platform.controller;

import com.blog.platform.blog.platform.dto.RegisterRequest;
import com.blog.platform.blog.platform.dto.UpdateUserRequest;
import com.blog.platform.blog.platform.dto.UserResponse;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user using RegisterRequest DTO and returns the created user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration payload",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Register Example",
                                    summary = "Sample request",
                                    value = """
                        {
                          "username": "john_doe",
                          "email": "john@example.com",
                          "password": "P@ssw0rd",
                          "userRole": "ROLE_USER",
                          "enabled": true
                        }
                        """
                            )
                    )
            )
            @RequestBody RegisterRequest request
    ) {
        UserResponse savedUser = userService.saveUserWithDto(request);
        return ResponseEntity.ok(savedUser);
    }

    // Get user by ID
    @Operation(summary = "Get user by ID", description = "Fetch a single user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.mapToUserResponse(userService.getUserByID(id)));
    }

    // Get user by username
    @Operation(summary = "Get user by username", description = "Returns user details using username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.mapToUserResponse(userService.getUserByUsername(username)));
    }

    // Get user by email
    @Operation(summary = "Get user by email", description = "Returns user details using email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/by-email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.mapToUserResponse(userService.getUserByEmail(email)));
    }

    // Update user profile (e.g., by current username)
    @PutMapping("/update/{currentUsername}")
    @Operation(summary = "Update user profile", description = "Updates user details such as username, email, or password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable String currentUsername,
            @RequestBody UpdateUserRequest request) {
        User updatedUser = userService.updateUserProfile(currentUsername, request);
        return ResponseEntity.ok(userService.mapToUserResponse(updatedUser));
    }

    // Soft delete by ID
    @DeleteMapping("/soft-delete/{id}")
    @Operation(summary = "Soft delete user by ID", description = "Marks a user as disabled by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User soft-deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> softDeleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User soft-deleted.");
    }

    // Hard delete by ID
    @DeleteMapping("/hard-delete/{id}")
    @Operation(summary = "Hard delete user by ID", description = "Permanently deletes a user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> hardDeleteUser(@PathVariable Long id) {
        userService.hardDeleteById(id);
        return ResponseEntity.ok("User permanently deleted.");
    }

    // Soft delete by any identifier (username, email, id)
    @DeleteMapping("/delete")
    @Operation(summary = "Delete user by identifier", description = "Soft deletes a user by ID, username, or email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User soft-deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid identifier type"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> deleteUserByType(
            @RequestParam String type,
            @RequestParam String value
    ) {
        userService.deleteUser(type, value);
        return ResponseEntity.ok("User soft-deleted by " + type + ": " + value);
    }

    // Get all users (admin only?)
    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users (admin access recommended)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users fetched successfully")
    })
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> responses = users.stream()
                .map(userService::mapToUserResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // Promote to admin
    @PostMapping("/promote/{username}")
    @Operation(summary = "Promote user to admin", description = "Assigns ADMIN role to a given user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User promoted to ADMIN"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> promoteToAdmin(@PathVariable String username) {
        userService.promoteToAdmin(username);
        return ResponseEntity.ok(username + " has been promoted to ADMIN.");
    }
}
