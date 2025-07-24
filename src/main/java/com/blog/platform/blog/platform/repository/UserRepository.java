package com.blog.platform.blog.platform.repository;

import com.blog.platform.blog.platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , Long> {
}
