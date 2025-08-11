package com.blog.platform.blog.platform.dto.UserDTO;

import com.blog.platform.blog.platform.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for user registration")
public class RegisterRequest {

    @NotBlank(message = "username should not be left blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(example = "john_doe", description = "Username for the user")
    private String username;

    @NotBlank(message = "email field is required")
    @Size(min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    @Email(message = "please provide a valid email")
    @Schema(example = "john@example.com", description = "Email address of the user")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters long")
    @Schema(example = "P@ssw0rd123", description = "Password for the user")
    private String password;

    @Schema(example = "https://example.com/profile.png", description = "URL for the user's profile image (optional)")
    private String profileImageUrl;

    // These values should be set to a default on the backend.

    @Schema(example = "ROLE_USER", description = "Role assigned to the user")
    private Role userRole;

    @Schema(example = "true", description = "Enable status of the user")
    private boolean enabled;

}
