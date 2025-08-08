package com.blog.platform.blog.platform.service;

import com.blog.platform.blog.platform.dto.CommentDTO.CommentRequest;
import com.blog.platform.blog.platform.dto.CommentDTO.CommentResponse;
import com.blog.platform.blog.platform.entity.Comment;
import com.blog.platform.blog.platform.entity.Post;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<CommentResponse> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .filter(comment -> comment.getParent() == null)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return mapToResponse(comment);
    }

    @Transactional
    public CommentResponse createComment(CommentRequest request, User user, Post post) {
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());

        return mapToResponse(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponse createReply(Long parentId, CommentRequest request, User user, Post post) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));

        Comment reply = new Comment();
        reply.setContent(request.getContent());
        reply.setUser(user);
        reply.setPost(post);
        reply.setParent(parent);
        reply.setCreatedAt(LocalDateTime.now());

        return mapToResponse(commentRepository.save(reply));
    }

    @Transactional
    public CommentResponse updateComment(Long id, String newContent) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setContent(newContent);
        return mapToResponse(commentRepository.save(comment));
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUsername(comment.getUser().getUsername());
        response.setProfileImageUrl(comment.getUser().getProfileImageUrl());
//        response.setReply(comment.getParent() != null);
        response.setLikes(0);
        response.setDislikes(0);

        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            List<CommentResponse> replies = comment.getReplies().stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            response.setReplies(replies);
        }

        return response;
    }

}
