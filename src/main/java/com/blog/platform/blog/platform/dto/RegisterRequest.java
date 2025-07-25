package com.blog.platform.blog.platform.dto;

import com.blog.platform.blog.platform.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for user registration")
public class RegisterRequest {

    @Schema(example = "john_doe", description = "Username for the user")
    private String username;

    @Schema(example = "john@example.com", description = "Email address of the user")
    private String email;

    @Schema(example = "P@ssw0rd", description = "Password for the user")
    private String password;

    @Schema(example = "ROLE_USER", description = "Role assigned to the user")
    private Role userRole;

    @Schema(example = "true", description = "Enable status of the user")
    private boolean enabled;
}
