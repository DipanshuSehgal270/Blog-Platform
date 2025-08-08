package com.blog.platform.blog.platform.config;

import com.blog.platform.blog.platform.entity.Comment;
import com.blog.platform.blog.platform.entity.Post;
import com.blog.platform.blog.platform.entity.Role;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.repository.CommentRepository;
import com.blog.platform.blog.platform.repository.PostRepository;
import com.blog.platform.blog.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Clear old data for a fresh start
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();

        // Create Users (at least 7)
        User adminUser = User.builder()
                .username("admin")
                .email("admin@test.com")
                .password(passwordEncoder.encode("adminpass"))
                .userrole(Role.ROLE_ADMIN)
                .enabled(true)
                .build();
        User user1 = User.builder()
                .username("john_doe")
                .email("john.doe@test.com")
                .password(passwordEncoder.encode("password123"))
                .userrole(Role.ROLE_USER)
                .enabled(true)
                .build();
        User user2 = User.builder()
                .username("jane_smith")
                .email("jane.smith@test.com")
                .password(passwordEncoder.encode("password123"))
                .userrole(Role.ROLE_USER)
                .enabled(true)
                .build();
        User user3 = User.builder()
                .username("peter_jones")
                .email("peter.jones@test.com")
                .password(passwordEncoder.encode("password123"))
                .userrole(Role.ROLE_USER)
                .enabled(true)
                .build();
        User user4 = User.builder()
                .username("mary_williams")
                .email("mary.williams@test.com")
                .password(passwordEncoder.encode("password123"))
                .userrole(Role.ROLE_USER)
                .enabled(true)
                .build();
        User user5 = User.builder()
                .username("robert_brown")
                .email("robert.brown@test.com")
                .password(passwordEncoder.encode("password123"))
                .userrole(Role.ROLE_USER)
                .enabled(true)
                .build();
        User user6 = User.builder()
                .username("linda_davis")
                .email("linda.davis@test.com")
                .password(passwordEncoder.encode("password123"))
                .userrole(Role.ROLE_USER)
                .enabled(true)
                .build();

        userRepository.saveAll(Arrays.asList(adminUser, user1, user2, user3, user4, user5, user6));

        // Create Posts
        Post post1 = new Post();
        post1.setTitle("First Post: The Dawn of a New Blog");
        post1.setContent("Welcome to my new blog! This is a test post to get things started. I hope you enjoy the content to come.");
        post1.setUser(user1);

        Post post2 = new Post();
        post2.setTitle("Spring Boot Pagination and Sorting Explained");
        post2.setContent("A deep dive into how to implement efficient pagination and sorting in a Spring Boot application using Spring Data JPA.");
        post2.setUser(adminUser);

        Post post3 = new Post();
        post3.setTitle("A Guide to Microservices Architecture");
        post3.setContent("Exploring the benefits and challenges of building applications with a microservices-based approach. A must-read for architects.");
        post3.setUser(user2);

        Post post4 = new Post();
        post4.setTitle("Debugging Common Java Errors");
        post4.setContent("A collection of common Java errors and how to debug them effectively using popular IDEs like IntelliJ and tools like JProfiler.");
        post4.setUser(user3);

        postRepository.saveAll(Arrays.asList(post1, post2, post3, post4));

        // Create Comments and Replies
        Comment comment1 = new Comment();
        comment1.setContent("This is a great first post, John! Looking forward to more.");
        comment1.setUser(user2);
        comment1.setPost(post1);

        Comment comment2 = new Comment();
        comment2.setContent("Thanks for the detailed explanation on pagination. It's very helpful.");
        comment2.setUser(user3);
        comment2.setPost(post2);

        // Nested reply to comment2
        Comment reply1 = new Comment();
        reply1.setContent("Glad you found it useful! Let me know if you have any questions.");
        reply1.setUser(adminUser);
        reply1.setPost(post2);
        reply1.setParent(comment2);

        Comment comment3 = new Comment();
        comment3.setContent("Microservices are the future! This guide is well-written.");
        comment3.setUser(user4);
        comment3.setPost(post3);

        // Nested reply to comment3
        Comment reply2 = new Comment();
        reply2.setContent("I agree! It's a complex topic, but the benefits are clear.");
        reply2.setUser(user5);
        reply2.setPost(post3);
        reply2.setParent(comment3);

        Comment reply3 = new Comment();
        reply3.setContent("Totally! The diagrams help a lot.");
        reply3.setUser(user6);
        reply3.setPost(post3);
        reply3.setParent(reply2);

        commentRepository.saveAll(Arrays.asList(comment1, comment2, reply1, comment3, reply2, reply3));
    }
}
