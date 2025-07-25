package com.blog.platform.blog.platform.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "username should not be left blank")
    @Size(min = 3, max = 100)
    @Column(nullable = false)
    private String username;

    @NotBlank(message = "email field is required")
    @Size(min = 3, max = 100)
    @Email(message = "please provide a valid email")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 3, max = 50)
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role userrole = Role.ROLE_USER;

    @Column(length = 255)
    private String profileImageUrl;

    @Column(length = 150)
    private String bio;

    @OneToMany(mappedBy = "author")
    @JsonIgnore  // Prevent circular loop
    private List<Post> posts;

    @Column(nullable = false)
    private boolean enabled = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


//    @PrePersist
//    public void prepersist(){
//        createdAt = LocalDateTime.now();
//        updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    public void preupdate(){
//        updatedAt = LocalDateTime.now();
//    }

}
