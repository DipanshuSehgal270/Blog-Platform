package com.blog.platform.blog.platform.service;

import com.blog.platform.blog.platform.dto.AuthResponse;
import com.blog.platform.blog.platform.dto.UserDTO.UserLoginRequest;
import com.blog.platform.blog.platform.dto.UserDTO.RegisterRequest;
import com.blog.platform.blog.platform.entity.Role;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.repository.UserRepository;
import com.blog.platform.blog.platform.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user, saves them to the database, and returns a JWT token.
     */
    public AuthResponse register(RegisterRequest request) {
        // Create a new user entity from the request
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Hash the password
                .userrole(request.getUserRole())
                .enabled(true)// Assign default role
                .build();

        // Save the new user to the database
        userRepository.save(user);

        // Generate a JWT token for the new user
        var jwtToken = jwtService.generateToken(user);

        // Return the token in the response
        return AuthResponse.builder().token(jwtToken).build();
    }

    /**
     * Authenticates an existing user and returns a JWT token if credentials are valid.
     */
    public AuthResponse login(UserLoginRequest request) {
        // Use the AuthenticationManager to validate the username and password.
        // This will automatically use our CustomUserDetailsService and PasswordEncoder.
        // If the credentials are bad, it will throw an exception.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // If we reach here, the user is authenticated.
        // Now, we find the user in the database to generate a token.
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(); // Should not happen if authentication was successful

        // Generate a JWT token for the authenticated user
        var jwtToken = jwtService.generateToken(user);

        // Return the token in the response
        return AuthResponse.builder().token(jwtToken).build();
    }
}
