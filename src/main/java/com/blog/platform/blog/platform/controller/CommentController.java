package com.blog.platform.blog.platform.controller;

import com.blog.platform.blog.platform.dto.CommentRequest;
import com.blog.platform.blog.platform.dto.CommentResponse;
import com.blog.platform.blog.platform.dto.PostResponse;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.repository.UserRepository;
import com.blog.platform.blog.platform.service.CommentService;
import com.blog.platform.blog.platform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    //TODO LEARN
    private final CommentService commentService;
    private final PostService postService;
    private final UserRepository userRepository;

    // 1. Get all comments for a specific post
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsForPost(@PathVariable Long postid)
    {
        List<CommentResponse> commentList = commentService.getCommentsByPostId(postid);
        return ResponseEntity.ok(commentList);
    }

    @GetMapping("posts/comment")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long commentid)
    {
        return ResponseEntity.ok(commentService.getCommentById(commentid));
    }

//    //TODO login system and then fetch the current user by authenticationprincipal
//    @PostMapping("/posts/{postId}/comments")
//    public ResponseEntity<CommentResponse> createNewComment(@PathVariable Long postId
//            ,@RequestBody CommentRequest request,
//            @RequestBody User user)
//    {
//        PostResponse response = postService.getPostById(postId);
//
//        String username =
//    }


}
