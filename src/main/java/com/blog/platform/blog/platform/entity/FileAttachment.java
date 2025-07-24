package com.blog.platform.blog.platform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "file_attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName; // Original name of uploaded file

    @Column(nullable = false)
    private String fileType; // image/png, application/pdf, etc.

    @Column(nullable = false)
    private String filePath; // Full path where file is stored (disk or cloud)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
