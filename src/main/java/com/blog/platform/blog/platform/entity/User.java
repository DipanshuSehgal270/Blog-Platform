package com.blog.platform.blog.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"posts"}) //  prevents recursion
@EqualsAndHashCode(exclude = {"posts"}) //  prevents recursion in comparisons
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "username should not be left blank")
    @Size(min = 3, max = 100)
    @Column(nullable = false, unique = true) // Added unique constraint
    private String username;

    @NotBlank(message = "email field is required")
    @Size(min = 3, max = 100)
    @Email(message = "please provide a valid email")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 3, max = 100) // Increased max size for hashed password
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role userrole ;

    @Column(length = 255)
    private String profileImageUrl;

    @Column(length = 150)
    private String bio;

    @OneToMany(mappedBy = "user") // Changed from 'author' to 'user' to match Comment entity
    @JsonIgnore
    private List<Post> posts;

    @Column(nullable = false)
    private boolean enabled = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // --- UserDetails Methods ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userrole.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Or add logic for account expiration
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Or add logic for account locking
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Or add logic for password expiration
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
