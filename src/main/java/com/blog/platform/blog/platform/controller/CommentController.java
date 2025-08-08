package com.blog.platform.blog.platform.controller;

import com.blog.platform.blog.platform.dto.CommentDTO.CommentRequest;
import com.blog.platform.blog.platform.dto.CommentDTO.CommentResponse;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.repository.UserRepository;
import com.blog.platform.blog.platform.service.CommentService;
import com.blog.platform.blog.platform.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    //TODO LEARN
    private final CommentService commentService;
    private final PostService postService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;



    // 1. Get all comments for a specific post
    @Operation(
            summary = "Get all comments for a specific post",
            description = "Returns a list of all top-level comments for a given post ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsForPost(@PathVariable Long postid)
    {
        List<CommentResponse> commentList = commentService.getCommentsByPostId(postid);
        return ResponseEntity.ok(commentList);
    }



    @Operation(
            summary = "Get a comment by its ID",
            description = "Returns a single comment by its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment found"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("posts/comment")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long commentid)
    {
        return ResponseEntity.ok(commentService.getCommentById(commentid));
    }



    @Operation(summary = "Create a new comment on a post",
            description = "Creates a new comment for the authenticated user on a specific post.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createNewComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal User user)
    {
        CommentResponse newComment = commentService.createComment(request , user , postId);

        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }


}
