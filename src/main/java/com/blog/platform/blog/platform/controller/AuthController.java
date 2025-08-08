package com.blog.platform.blog.platform.controller;

import com.blog.platform.blog.platform.dto.AuthResponse;
import com.blog.platform.blog.platform.dto.UserDTO.UserLoginRequest;
import com.blog.platform.blog.platform.dto.UserDTO.RegisterRequest;
import com.blog.platform.blog.platform.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user using RegisterRequest DTO and returns the created user as AuthResponse DTO"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(
            summary = "login an existing user",
            description = "login into your account using UserLoginRequest DTO and get response as AuthResponse DTO"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}

