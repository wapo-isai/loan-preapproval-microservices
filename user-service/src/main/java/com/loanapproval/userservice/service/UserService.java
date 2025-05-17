package com.loanapproval.userservice.service;

import java.util.*;
import java.util.stream.Collectors;

import com.loanapproval.userservice.dto.AuthResponse;
import com.loanapproval.userservice.dto.SignInRequest;
import com.loanapproval.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.loanapproval.userservice.exception.EmailAlreadyExistsException;
import com.loanapproval.userservice.dto.RegisterRequest;
import com.loanapproval.userservice.dto.UpdateUserRequest;
import com.loanapproval.userservice.exception.UserNotFoundException;
import com.loanapproval.userservice.persistence.User;
import com.loanapproval.userservice.persistence.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email address " + request.getEmail() + " already in use.");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        user.setEmploymentStatus(request.getEmploymentStatus());
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER"); // Default role
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public Optional<AuthResponse> authenticateUser(SignInRequest signInRequest) {
        Optional<User> userOptional = userRepository.findByEmail(signInRequest.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(signInRequest.getPassword(), user.getPasswordHash())) {
                String token = jwtUtil.generateToken(user);
                List<String> roles = user.getRoles().stream().collect(Collectors.toList());
                AuthResponse authResponse = new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getStreetAddress(), user.getCity(), user.getState(), user.getZipCode(), user.getEmploymentStatus(), user.getEmploymentDetails(), roles);
                return Optional.of(authResponse);
            }
        }
        return Optional.empty(); // Authentication failed
    }

    public User updateProfile(String id, UpdateUserRequest request) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (StringUtils.hasText(request.getFullName())) {
            userToUpdate.setFullName(request.getFullName());
        }
        if (StringUtils.hasText(request.getStreetAddress())) {
            userToUpdate.setStreetAddress(request.getStreetAddress());
        }
        if (StringUtils.hasText(request.getCity())) {
            userToUpdate.setCity(request.getCity());
        }
        if (StringUtils.hasText(request.getState())) {
            userToUpdate.setState(request.getState());
        }
        if (StringUtils.hasText(request.getZipCode())) {
            userToUpdate.setZipCode(request.getZipCode());
        }
        if (StringUtils.hasText(request.getEmploymentStatus())) {
            userToUpdate.setEmploymentStatus(request.getEmploymentStatus());
        }
        if (StringUtils.hasText(request.getEmploymentDetails())) {
            userToUpdate.setEmploymentDetails(request.getEmploymentDetails());
        }

        userToUpdate.setUpdatedAt(new Date());

        return userRepository.save(userToUpdate);
    }
}
