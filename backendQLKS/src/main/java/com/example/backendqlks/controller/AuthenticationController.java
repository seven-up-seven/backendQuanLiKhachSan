package com.example.backendqlks.controller;

import com.example.backendqlks.dto.authentication.LoginDto;
import com.example.backendqlks.dto.authentication.ResponseLoginDto;
import com.example.backendqlks.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @GetMapping
    private ResponseEntity<?> authenticate(@Valid @RequestBody LoginDto loginDto, BindingResult result) {
        try {
            if(result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }

            var token = authenticationService.authenticate(loginDto);
            if (token != null) {
                return ResponseEntity.ok(new ResponseLoginDto(token, "Login successful"));
            } else {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during authentication: " + e.getMessage());
        }
    }
}
