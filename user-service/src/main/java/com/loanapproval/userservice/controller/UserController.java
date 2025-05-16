package com.loanapproval.userservice.controller;

import com.loanapproval.userservice.dto.AuthResponse;
import com.loanapproval.userservice.dto.SignInRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loanapproval.userservice.dto.RegisterRequest;
import com.loanapproval.userservice.dto.UpdateUserRequest;
import com.loanapproval.userservice.persistence.User;
import com.loanapproval.userservice.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.registerUser(registerRequest);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully! Please login.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        Optional<AuthResponse> authResponseOptional = userService.authenticateUser(signInRequest);

        if (authResponseOptional.isPresent()) {
            return ResponseEntity.ok(authResponseOptional.get());
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse); // <--- Return JSON error object
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getProfile(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateProfile(@PathVariable String id,
                                              @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateProfile(id, request));
    }
}
