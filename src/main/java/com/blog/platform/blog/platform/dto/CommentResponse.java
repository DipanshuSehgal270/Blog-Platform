package com.blog.platform.blog.platform.dto;

import com.blog.platform.blog.platform.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    @Schema(description = "Comment author's profile image")
    private String profileImageUrl;

    @Schema(description = "Comment author's username")
    private String username;

    @Schema(description = "Content of the comment")
    private String content;

    @Schema(description = "Number of likes on this comment")
    private int likes;

    @Schema(description = "Number of dislikes on this comment")
    private int dislikes;

    @Schema(description = "If this comment is a reply to another")
    private boolean isReply;

    @Schema(description = "Timestamp of comment creation")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "List of replies to this comment (if any)")
    private List<CommentResponse> replies;
}
