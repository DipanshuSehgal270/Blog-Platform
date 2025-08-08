package com.blog.platform.blog.platform.service;

import com.blog.platform.blog.platform.dto.CommentDTO.CommentRequest;
import com.blog.platform.blog.platform.dto.CommentDTO.CommentResponse;
import com.blog.platform.blog.platform.entity.Comment;
import com.blog.platform.blog.platform.entity.Post;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.exception.ResourceNotFoundException;
import com.blog.platform.blog.platform.mapper.CommentMapper;
import com.blog.platform.blog.platform.repository.CommentRepository;
import com.blog.platform.blog.platform.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        // You should fetch the post first to ensure it exists before querying for comments
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        List<Comment> comments = commentRepository.findByPostId(postId);

        // Filter and map using the dedicated mapper
        return comments.stream()
                .filter(comment -> comment.getParent() == null) // Only show top-level comments
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommentResponse getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        return commentMapper.toResponse(comment);
    }

    @Transactional
    public CommentResponse createComment(CommentRequest request, User user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(user);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toResponse(savedComment);
    }

    @Transactional
    public CommentResponse createReply(Long parentId, CommentRequest request, User user, Long postId) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent Comment", "id", parentId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comment reply = new Comment();
        reply.setContent(request.getContent());
        reply.setUser(user);
        reply.setPost(post);
        reply.setParent(parent);

        Comment savedReply = commentRepository.save(reply);
        return commentMapper.toResponse(savedReply);
    }

    @Transactional
    public CommentResponse updateComment(Long id, String newContent) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));

        comment.setContent(newContent);
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toResponse(updatedComment);
    }

    @Transactional
    public void deleteComment(Long id) {
        // Find the comment first to ensure it exists before deleting.
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));

        commentRepository.delete(comment);
    }
}