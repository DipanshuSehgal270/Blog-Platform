package com.blog.platform.blog.platform.dto.UserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Schema(example = "john_doe", description = "Username for the user")
    private String username;
    @Schema(example = "john@example.com", description = "Email address of the user")
    private String email;
    @Schema(example = "P@ssw0rd", description = "Password for the user")
    private String password;
}
