package com.blog.platform.blog.platform.dto.UserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    @Schema(example = "1", description = "Unique identifier of the user")
    private Long id;

    @Schema(example = "john_doe", description = "Username for the user")
    private String username;

    @Schema(example = "john@example.com", description = "Email address of the user")
    private String email;

    @Schema(example = "ROLE_USER", description = "Role assigned to the user")
    private String role;

    @Schema(example = "true", description = "Whether the user is enabled or not")
    private boolean enabled;

    @Schema(example = "https://cdn.blog.com/profile/john_doe.jpg", description = "URL to user's profile image")
    private String profileImageUrl;

    @Schema(example = "I’m a backend dev who loves Spring Boot!", description = "User bio or description")
    private String bio;

}

