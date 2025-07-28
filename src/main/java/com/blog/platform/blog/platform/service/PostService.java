package com.blog.platform.blog.platform.service;

import com.blog.platform.blog.platform.dto.AuthorInfoDto;
import com.blog.platform.blog.platform.dto.PostRequest;
import com.blog.platform.blog.platform.dto.PostResponse;
import com.blog.platform.blog.platform.entity.Post;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.exception.ResourceNotFoundException;
import com.blog.platform.blog.platform.repository.PostRepository;
import com.blog.platform.blog.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // Create a new post
    public PostResponse createPost(String username, PostRequest request) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setUser(author);

        Post savedPost = postRepository.save(post);
        return mapToPostResponse(savedPost);
    }

    // Get post by ID
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToPostResponse(post);
    }

    // Get all posts
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToPostResponse)
                .collect(Collectors.toList());
    }

    // Update a post
    public PostResponse updatePost(Long postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToPostResponse(updatedPost);
    }

    // Delete post
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        postRepository.delete(post);
    }

    // Map Post to PostResponse
    private PostResponse mapToPostResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());

        AuthorInfoDto authorDto = new AuthorInfoDto(
                post.getUser().getUsername(),
                post.getUser().getProfileImageUrl()
        );

        response.setAuthor(authorDto);
        return response;
    }


    //TODO HOMEPAGE METHOD
}
