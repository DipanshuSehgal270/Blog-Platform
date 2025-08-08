package com.blog.platform.blog.platform.service;

import com.blog.platform.blog.platform.dto.PostDTO.PostRequest;
import com.blog.platform.blog.platform.dto.PostDTO.PostResponse;
import com.blog.platform.blog.platform.entity.Post;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.exception.ResourceNotFoundException;
import com.blog.platform.blog.platform.mapper.PostMapper;
import com.blog.platform.blog.platform.repository.PostRepository;
import com.blog.platform.blog.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    // Create a new post
    @Transactional
    public PostResponse createPost(String username, PostRequest request) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // 2. Use the mapper to convert the DTO to an entity
        Post post = postMapper.toEntity(request);
        post.setUser(author);

        Post savedPost = postRepository.save(post);

        // 3. Use the mapper to convert the saved entity to a DTO
        return postMapper.toResponse(savedPost);
    }

    // Get post by ID
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        // 4. Use the mapper to convert the entity to a DTO
        return postMapper.toResponse(post);
    }

    // Get all posts with pagination and sorting
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> postPage = postRepository.findAll(pageable);
        List<Post> posts = postPage.getContent();

        // 5. Use the mapper to convert the list of entities to a list of DTOs
        return posts.stream()
                .map(postMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Update a post
    @Transactional
    public PostResponse updatePost(Long postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        Post updatedPost = postRepository.save(post);

        // 6. Use the mapper for the final conversion
        return postMapper.toResponse(updatedPost);
    }

    // Delete post
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        postRepository.delete(post);
    }

    // 7. Remove the manual mapToPostResponse method entirely.
    // private PostResponse mapToPostResponse(Post post) {...}

    //TODO HOMEPAGE METHOD
}