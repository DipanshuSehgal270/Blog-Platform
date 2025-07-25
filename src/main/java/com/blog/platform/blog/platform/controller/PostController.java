package com.blog.platform.blog.platform.controller;

import com.blog.platform.blog.platform.dto.PostRequest;
import com.blog.platform.blog.platform.dto.PostResponse;
import com.blog.platform.blog.platform.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //Create a post
    @Operation(
            summary = "Create a new post",
            description = "Creates a post authored by the given username with title and content"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post created successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid post data")
    })
    @PostMapping("/create/{username}")
    public ResponseEntity<PostResponse> createPost(
            @PathVariable String username,
            @RequestBody PostRequest request
    ) {
        PostResponse created = postService.createPost(username, request);
        return ResponseEntity.ok(created);
    }

    // Get post by ID
    @Operation(summary = "Get post by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post found"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(
            @Parameter(description = "ID of the post") @PathVariable Long id
    ) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    // Get all posts
    @Operation(summary = "Get all posts")
    @ApiResponse(responseCode = "200", description = "List of all posts")
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // Update a post
    @Operation(summary = "Update an existing post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post updated"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PutMapping("/update/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequest request
    ) {
        PostResponse updated = postService.updatePost(postId, request);
        return ResponseEntity.ok(updated);
    }

    // Delete a post
    @Operation(summary = "Delete a post by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post deleted"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("Post deleted successfully.");
    }
}
