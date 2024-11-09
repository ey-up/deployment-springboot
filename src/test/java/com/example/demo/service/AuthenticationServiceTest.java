package com.example.demo.service;//package com.example.demo.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//package com.example.demo.service;
//
//import com.example.demo.dto.request.LoginUserRequest;
//import com.example.demo.dto.request.RegisterUserRequest;
//import com.example.demo.dto.response.LoginUserResponse;
//import com.example.demo.exception.EmailAlreadyTakenException;
//import com.example.demo.exception.UserNotActiveException;
//import com.example.demo.exception.UserNotFoundException;
//import com.example.demo.model.User;
//import com.example.demo.repository.UserRepository;
//import com.example.demo.security.JwtService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class AuthenticationServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @Mock
//    private JwtService jwtService;
//
//    @InjectMocks
//    private AuthenticationService authenticationService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void signup_ShouldCreateNewUser_WhenEmailIsNotTaken() {
//        // Arrange
//        RegisterUserRequest request = new RegisterUserRequest("test@example.com", "password");
//        User user = new User();
//        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
//        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        // Act
//        User result = authenticationService.signup(request);
//
//        // Assert
//        assertNotNull(result);
//        verify(userRepository).save(any(User.class));
//    }
//
//    @Test
//    void signup_ShouldThrowEmailAlreadyTakenException_WhenEmailIsAlreadyInUse() {
//        // Arrange
//        RegisterUserRequest request = new RegisterUserRequest("test@example.com", "password");
//        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
//
//        // Act & Assert
//        assertThrows(EmailAlreadyTakenException.class, () -> authenticationService.signup(request));
//        verify(userRepository, never()).save(any(User.class));
//    }
//
//    @Test
//    void login_ShouldReturnLoginResponse_WhenCredentialsAreValidAndUserIsActive() {
//        // Arrange
//        LoginUserRequest request = new LoginUserRequest("test@example.com", "password");
//        User user = new User();
//        user.setEmail(request.getEmail());
//        user.setActive(true);
//        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
//        when(jwtService.generateToken(user)).thenReturn("jwtToken");
//        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");
//        when(jwtService.getExpirationTime()).thenReturn(3600L);
//
//        // Act
//        LoginUserResponse response = authenticationService.login(request);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("jwtToken", response.getJwt());
//        assertEquals("refreshToken", response.getRefreshToken());
//        assertEquals(3600L, response.getExpirationTime());
//    }
//
//    @Test
//    void login_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
//        // Arrange
//        LoginUserRequest request = new LoginUserRequest("test@example.com", "password");
//        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(UserNotFoundException.class, () -> authenticationService.login(request));
//    }
//
//    @Test
//    void login_ShouldThrowUserNotActiveException_WhenUserIsInactive() {
//        // Arrange
//        LoginUserRequest request = new LoginUserRequest("test@example.com", "password");
//        User user = new User();
//        user.setEmail(request.getEmail());
//        user.setActive(false);
//        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
//
//        // Act & Assert
//        assertThrows(UserNotActiveException.class, () -> authenticationService.login(request));
//    }
//}
