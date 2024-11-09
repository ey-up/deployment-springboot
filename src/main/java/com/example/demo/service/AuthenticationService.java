package com.example.demo.service;

import com.example.demo.dto.request.LoginUserRequest;
import com.example.demo.dto.request.RegisterUserRequest;
import com.example.demo.dto.response.LoginUserResponse;
import com.example.demo.exception.EmailAlreadyTakenException;
import com.example.demo.exception.UserNotActiveException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User signup(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyTakenException(request.getEmail() + " email address is already in use");
        }
        User user = User.create(request, passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        User user = userRepository.findByEmail(loginUserRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + loginUserRequest.getEmail()));

        if (!user.isActive()) {
            throw new UserNotActiveException("User account is inactive for email: " + user.getEmail());
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserRequest.getEmail(), loginUserRequest.getPassword()));

        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        long expirationTime = jwtService.getExpirationTime();
        logger.info("User '{}' logged in successfully.", user.getEmail());

        return new LoginUserResponse(jwt, refreshToken, expirationTime);
    }

}
