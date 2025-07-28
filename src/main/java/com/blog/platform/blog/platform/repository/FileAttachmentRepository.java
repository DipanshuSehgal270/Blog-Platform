package com.blog.platform.blog.platform.repository;

import com.blog.platform.blog.platform.entity.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment , Long> {
}
