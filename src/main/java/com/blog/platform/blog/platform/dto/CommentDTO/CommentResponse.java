package com.blog.platform.blog.platform.dto.CommentDTO;

import com.blog.platform.blog.platform.dto.ReplyDTO.SimpleReplyDTO;
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

    private String profileImageUrl;
    private String username;
    private String content;
    private int likes;
    private int dislikes;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    // Only one level of replies
    private List<SimpleReplyDTO> replies;
}
