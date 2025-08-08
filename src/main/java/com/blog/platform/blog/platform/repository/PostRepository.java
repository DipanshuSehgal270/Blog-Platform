package com.blog.platform.blog.platform.repository;

import com.blog.platform.blog.platform.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Post findPostById(Long postId);
}
