package com.blog.platform.blog.platform.service;

import com.blog.platform.blog.platform.dto.CommentDTO.CommentRequest;
import com.blog.platform.blog.platform.dto.CommentDTO.CommentResponse;
import com.blog.platform.blog.platform.dto.ReplyDTO.SimpleReplyDTO;
import com.blog.platform.blog.platform.entity.Comment;
import com.blog.platform.blog.platform.entity.Post;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.exception.ResourceNotFoundException;
import com.blog.platform.blog.platform.mapper.CommentMapper;
import com.blog.platform.blog.platform.repository.CommentRepository;
import com.blog.platform.blog.platform.repository.PostRepository;
import com.blog.platform.blog.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
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

    public CommentResponse createComment(CommentRequest request, Long postId) {
        User user = getCurrentUser(); // this is giving error

        if (user == null) {
            // Handle the case where the user is not authenticated.
            // This scenario should be prevented by your security configuration,
            // but this check adds an extra layer of safety.
            throw new IllegalStateException("Authenticated user not found.");
        }
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
    public SimpleReplyDTO createReply(Long parentId, CommentRequest request) {

        User user = getCurrentUser();

        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent Comment", "id", parentId));

        Comment reply = Comment.builder()
                .content(request.getContent())
                .user(user)
                .post(parent.getPost())
                .parent(parent)
                .build();

        Comment savedReply = commentRepository.save(reply);

        // Return only the reply info
        return commentMapper.toSimpleReply(savedReply);
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

    public boolean isPostOwnerByCommentId(Long commentId, User user) {
        // 1. Find the comment by its ID.
        // We use orElse(null) here because the PreAuthorize check should not throw an exception.
        Comment comment = commentRepository.findById(commentId).orElse(null);

        // 2. If the comment exists, check if the post it belongs to is owned by the user.
        return comment != null && comment.getPost().getUser().getId().equals(user.getId());
    }

    public boolean isCommentOwner(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        return comment != null && comment.getUser().getId().equals(user.getId());
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

}