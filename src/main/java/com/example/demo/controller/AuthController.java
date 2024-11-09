package com.example.demo.controller;

import com.example.demo.dto.request.LoginUserRequest;
import com.example.demo.dto.request.RegisterUserRequest;
import com.example.demo.dto.response.LoginUserResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.model.User;
import com.example.demo.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@Valid @RequestBody LoginUserRequest LoginUserRequest) {
        LoginUserResponse loginUserResponse = authenticationService.login(LoginUserRequest);
        return ResponseEntity.ok(loginUserResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        User savedUser = authenticationService.signup(registerUserRequest);
        return ResponseEntity.ok(UserResponse.create(savedUser));
    }
}