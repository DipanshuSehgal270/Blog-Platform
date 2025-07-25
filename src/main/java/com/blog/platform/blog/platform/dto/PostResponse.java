package com.blog.platform.blog.platform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Response DTO representing a blog post with author, metadata and stats")
public class PostResponse {

    @Schema(description = "Author of the post")
    private AuthorInfoDto author;

    @Schema(description = "Title of the post", example = "Spring Boot Tips & Tricks")
    private String title;

    @Schema(description = "Main content of the post", example = "Here are 10 cool tricks in Spring Boot...")
    private String content;

    @Schema(description = "Date and time when the post was created", example = "2025-07-24T15:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Number of likes for the post", example = "15")
    private int likeCount = 0;

    @Schema(description = "Number of dislikes for the post", example = "2")
    private int dislikeCount = 0;

    @Schema(description = "List of attachment file URLs (e.g. images, videos)")
    private List<String> attachmentUrls;
}
