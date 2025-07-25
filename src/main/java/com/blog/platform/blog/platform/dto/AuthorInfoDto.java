package com.blog.platform.blog.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@Schema(description = "Minimal information about a post author")
public class AuthorInfoDto {
    @Schema(example = "john_doe", description = "Username of the post author")
    private String username;

    @Schema(example = "https://cdn.example.com/img/john_doe.jpg")
    private String profileImageUrl;
}
