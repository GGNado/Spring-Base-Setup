package com.giggi.basesetup.controller;

import com.giggi.basesetup.dto.request.auth.LoginRequest;
import com.giggi.basesetup.dto.request.auth.RegisterRequest;
import com.giggi.basesetup.dto.response.MessageResponse;
import com.giggi.basesetup.dto.response.jwt.JwtResponse;
import com.giggi.basesetup.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthServiceImpl authService;

    /**
     * Authenticate user and return JWT token.
     */
    @PostMapping("/signin")
    @Operation(summary = "User login", description = "Authenticate user credentials and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getUsernameOrEmail());

        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            log.info("Login successful for user: {}", loginRequest.getUsernameOrEmail());
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            log.error("Login failed for user: {} - Error: {}", loginRequest.getUsernameOrEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(MessageResponse.error("Invalid username/email or password"));
        }
    }

    /**
     * Register a new user.
     */
    @PostMapping("/signup")
    @Operation(summary = "User registration", description = "Register a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data or user already exists",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        System.out.println(signUpRequest.toString());
        log.info("Registration attempt for user: {}", signUpRequest.getUsername());

        try {
            MessageResponse response = authService.registerUser(signUpRequest);

            if (response.isSuccess()) {
                log.info("Registration successful for user: {}", signUpRequest.getUsername());
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                log.warn("Registration failed for user: {} - Error: {}", signUpRequest.getUsername(), response.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("Registration failed for user: {} - Error: {}", signUpRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.error("Error registering user: " + e.getMessage()));
        }
    }
}