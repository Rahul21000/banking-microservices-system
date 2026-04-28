package com.rbank.auth_service.controller;

import com.rbank.auth_service.auth.JwtAuthenticationFilter;
import com.rbank.auth_service.auth.JwtUtil;
import com.rbank.auth_service.dto.LoginRequestDTO;
import com.rbank.auth_service.dto.ResponseDTO;
import com.rbank.auth_service.dto.CustomerRegistrationDTO;
import com.rbank.auth_service.exception.InvalidInputException;
import com.rbank.auth_service.service.UserDetailsImpl;
import com.rbank.auth_service.client.UserServiceClient;
import com.rbank.auth_service.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private final ResponseUtils responseUtils;
    @Autowired
    private final UserServiceClient userServiceClient;
    @Autowired
    private  final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, ResponseUtils responseUtils, UserServiceClient userServiceClient, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.responseUtils = responseUtils;
        this.userServiceClient = userServiceClient;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@Valid @RequestBody CustomerRegistrationDTO customerRegistrationDTO) {
        if (customerRegistrationDTO.getUsername() == null || customerRegistrationDTO.getUsername().isEmpty()) {
            throw new InvalidInputException("Username is required");
        }
        if (!EMAIL_PATTERN.matcher(customerRegistrationDTO.getEmail()).matches()) {
            throw new InvalidInputException("Invalid email format");
        }
        if (customerRegistrationDTO.getPassword().length() < 8 || !customerRegistrationDTO.getPassword().matches(".*[A-Z].*")) {
            throw new InvalidInputException("Password must be at least 8 characters and contain at least one uppercase letter");
        }

        LOGGER.info("Registering user: {}", customerRegistrationDTO.getUsername());

        ResponseEntity<ResponseDTO> response = userServiceClient.createCustomer(customerRegistrationDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // Endpoint for user login and JWT token generation
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LOGGER.info("User attempting login: {}", loginRequestDTO.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (!authentication.isAuthenticated()) {
            return responseUtils.handleResponseInPayload(null, "Invalid username or password", 400, HttpStatus.BAD_REQUEST);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwtToken = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getCustomerId(), roles);
        LOGGER.info("JWT Token generated for user: {}", userDetails.getUsername());

        Map<String, String> responsePayload = new LinkedHashMap<>();
        responsePayload.put("username", userDetails.getUsername());
        responsePayload.put("token", jwtToken);
        responsePayload.put("role", roles.toString());

        return responseUtils.handleResponseInPayload(responsePayload, "Successfully logged in", 200, HttpStatus.OK);
    }


    @PostMapping("/signout")
    public ResponseEntity<ResponseDTO> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtAuthenticationFilter.extractToken(request);
        if (token != null) {
            LOGGER.info("Invalidating token for logout: {}", token);
        }

        SecurityContextHolder.clearContext();
        return responseUtils.handleResponseInPayload(null, "Successfully logged out", 200, HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestParam String token) {
        LOGGER.info("make calling api");
        try {
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("valid", false, "error", "Invalid or expired token"));
            }

            List<String> roles = jwtUtil.extractRoles(token);

            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "roles", roles
            ));
        } catch (Exception e) {
            LOGGER.error("Token validation failed: {}", e.getMessage());
            return ResponseEntity.status(401).body(Map.of("valid", false, "error", "Invalid or expired token"));
        }
    }

}
