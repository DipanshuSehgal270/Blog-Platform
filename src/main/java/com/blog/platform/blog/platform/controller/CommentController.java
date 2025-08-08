package com.blog.platform.blog.platform.controller;

import com.blog.platform.blog.platform.dto.CommentDTO.CommentRequest;
import com.blog.platform.blog.platform.dto.CommentDTO.CommentResponse;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.service.CommentService;
import com.blog.platform.blog.platform.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;
    private final PostService postService;


    // A note about dependency injection:
    // It's a best practice to only inject services, not repositories, into controllers.
    // The `commentRepository` dependency should be removed from here.


    // Get all comments for a specific post
    @Operation(
            summary = "Get all comments for a specific post",
            description = "Returns a list of all top-level comments for a given post ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/posts/{postId}") // Corrected the path for clarity
    public ResponseEntity<List<CommentResponse>> getCommentsForPost(@PathVariable Long postId) {
        List<CommentResponse> commentList = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(commentList);
    }




    // Get a comment by its ID
    @Operation(
            summary = "Get a comment by its ID",
            description = "Returns a single comment by its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment found"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/{commentId}") // Corrected the path
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }




    // Create a new comment on a post
    @Operation(
            summary = "Create a new comment on a post",
            description = "Creates a new comment for the authenticated user on a specific post."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping("/posts/{postId}")
    public ResponseEntity<CommentResponse> createNewComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request) {

        CommentResponse newComment = commentService.createComment(request, postId);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);

    }




    // Create a new reply to an existing comment
    @Operation(
            summary = "Create a new reply",
            description = "Creates a reply to a parent comment for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reply created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Parent comment not found")
    })
    @PostMapping("/{parentId}/replies")
    public ResponseEntity<CommentResponse> createReply(
            @PathVariable Long parentId,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal User user
    ) {
        // The service will handle fetching the post ID from the parent comment.
        CommentResponse reply = commentService.createReply(parentId, request, user);
        return new ResponseEntity<>(reply, HttpStatus.CREATED);
    }




    // Update a comment
    @Operation(
            summary = "Update a comment by ID",
            description = "Updates the content of an existing comment. Requires ownership or admin privileges."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PutMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @commentService.isCommentOwner(#commentId, authentication.principal)")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal User user // The user parameter is not needed by the service method, so it can be removed.
    ) {
        CommentResponse updatedComment = commentService.updateComment(commentId, request.getContent());
        return ResponseEntity.ok(updatedComment);
    }




    // Delete a comment
    @Operation(
            summary = "Delete a comment by ID",
            description = "Deletes a comment. Requires ownership of the comment, ownership of the post, or admin privileges."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @commentService.isCommentOwner(#commentId, authentication.principal) || @postService.isPostOwnerByCommentId(#commentId, authentication.principal)")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully.");
    }
}