package com.blog.platform.blog.platform.repository;

import ch.qos.logback.core.status.Status;
import com.blog.platform.blog.platform.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment , Long> {
    List<Comment> findByPostId(Long postId);

    // Replies to a specific comment (for thread display)
    List<Comment> findByParentId(Long parentId);

    // All comments made by a specific user (optional)
    List<Comment> findByUserId(Long userId);

    // Optional: delete all comments for a post
    void deleteByPostId(Long postId);

    // Optional: delete all comments by a user (if user is deleted)
    void deleteByUserId(Long userId);

}
