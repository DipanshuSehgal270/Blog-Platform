package com.blog.platform.blog.platform.service;

import com.blog.platform.blog.platform.dto.UserDTO.RegisterRequest;
import com.blog.platform.blog.platform.dto.UserDTO.UpdateUserRequest;
import com.blog.platform.blog.platform.dto.UserDTO.UserResponse;
import com.blog.platform.blog.platform.entity.Role;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.exception.ResourceNotFoundException;
import com.blog.platform.blog.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class UserService {

    //CRUD user , get user by id , username , email

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    //get user by ID
    public User getUserByID(Long id)
    {
        return userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",id));
    }

    // get user by Username
    public User getUserByUsername(String username)
    {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found with username: "+username));
    }

    // get user by email
    public User getUserByEmail(String email)
    {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found with email: "+email));
    }

    //save user in database
    public User saveUser(User user)
    {
        return userRepository.save(user);
    }

    //TODO PASSWORD ENCODER
    public UserResponse UpdateUserWithDto(RegisterRequest request)
    {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setUserrole(request.getUserRole());
        user.setEnabled(true);

        User saved = userRepository.save(user);

        return mapToUserResponse(saved);
    }

    public UserResponse mapToUserResponse(User user)
    {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserrole().name(),
                user.isEnabled(),
                user.getBio(),
                user.getProfileImageUrl()
        );
    }

    //updates the data user wants
    //TODO PASSWORD ENCODER
    public User updateUserProfile(String currentusername , UpdateUserRequest request)
    {
        User user = userRepository.findByUsername(currentusername)
                .orElseThrow(()-> new RuntimeException("User not found"));

        // Validate new username (if changed)
        if(request.getUsername()!=null && !request.getUsername().equals(user.getUsername()))
        {
            if(userRepository.existsByUsername(request.getUsername()))
            {
                throw  new RuntimeException("Username '" + request.getUsername() + "' is already taken.");
            }
            user.setUsername(request.getUsername());
        }

        // Validate new email (if changed)
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email '" + request.getEmail() + "' is already registered.");
            }
            user.setEmail(request.getEmail());
        }

        // Update password if provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }

        return userRepository.save(user);
    }

    //soft delete the user
    public void deleteUserById(Long id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",id));

        user.setEnabled(false); //soft delete
        userRepository.save(user);
    }

    //completely delete the user
    public void hardDeleteById(Long id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("user","id",id));
        userRepository.delete(user);
    }

    //soft delete user with id or username or email
    public void deleteUser(String type , String value)
    {
        User user;

        switch (type.toLowerCase())
        {
            case "id" -> user = userRepository.findById(Long.parseLong(value))
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", value));
            case "email" -> user = userRepository.findByEmail(value)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", value));
            case "username" -> user = userRepository.findByUsername(value)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "username", value));
            default -> throw new IllegalArgumentException("Invalid identifier type: " + type);
        }

        user.setEnabled(false);
        userRepository.save(user);
    }

    public User promoteToAdmin(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserrole(Role.ROLE_ADMIN);
        return userRepository.save(user);
    }

}
