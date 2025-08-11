package com.blog.platform.blog.platform.entity;

import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

public class Reply {

    private Long id;

    private String profileUrl;

    private String replierName;

    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private String parent;
}
