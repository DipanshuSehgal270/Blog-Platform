package com.blog.platform.blog.platform.service;

import com.blog.platform.blog.platform.dto.UpdateUserRequest;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.exception.ResourceNotFoundException;
import com.blog.platform.blog.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    //CRUD user , get user by id , username , email
    @Autowired
    private UserRepository userRepository;

    public User getUserByID(Long id)
    {
        return userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",id));
    }

    public User getUserByUsername(String Username)
    {
        return userRepository.findByUsername(Username)
                .orElseThrow(()-> new RuntimeException("User not found with username: "+Username));
    }

    public User getUserByEmail(String email)
    {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found with email: "+email));
    }

    public User saveUser(User user)
    {
        return userRepository.save(user);
    }

    public User updateUserProfile(String currentusername , UpdateUserRequest request)
    {
        User user = userRepository.findByUsername(currentusername)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(request.getUsername() != null) user.setUsername(request.getUsername());
        if(request.getEmail() != null) user.setEmail(request.getEmail());
        if(request.getPassword() != null) user.setPassword(request.getPassword());

        return userRepository.save(user);
    }






}
