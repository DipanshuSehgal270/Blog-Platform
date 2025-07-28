package com.blog.platform.blog.platform.repository;

import com.blog.platform.blog.platform.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction,Long> {
}
