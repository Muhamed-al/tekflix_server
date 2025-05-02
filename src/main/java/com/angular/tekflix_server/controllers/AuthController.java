package com.angular.tekflix_server.controllers;


import com.angular.tekflix_server.dto.ApiResponse;
import com.angular.tekflix_server.dto.LoginResponse;
import com.angular.tekflix_server.models.ERole;
import com.angular.tekflix_server.models.User;
import com.angular.tekflix_server.repository.UserRepository;
import com.angular.tekflix_server.security.JWTGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final  AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            loginDto.getName(), loginDto.getPassword()
                    )
            );
            String token = jwtGenerator.generateToken(authentication);
            return ResponseEntity.ok(
                    new LoginResponse("Login successful", token)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Invalid credentials"));
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register( @RequestBody User registerDto) {
        // Check if the email already exists
        if (userRepository.existsByName(registerDto.getName())) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Email Déjà utilisé!"));
        }
        // Create a new user
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        user.setRole(ERole.UTILISATEUR);

        // Save the user
        try {
            userRepository.save(user);
        } catch (Exception e) {
            logger.error("user registration failed", e);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to register user  "));
        }
        return  ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("User registered successfully!"));
    }
}
