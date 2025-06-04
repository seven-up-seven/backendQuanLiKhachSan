package com.example.backendqlks.controller;

import com.example.backendqlks.dao.AccountRepository;
import com.example.backendqlks.dto.authentication.LoginDto;
import com.example.backendqlks.dto.authentication.RefreshResultDto;
import com.example.backendqlks.dto.authentication.RefreshTokenDto;
import com.example.backendqlks.dto.authentication.ResponseLoginDto;
import com.example.backendqlks.entity.Account;
import com.example.backendqlks.entity.UserRole;
import com.example.backendqlks.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authentication")
public class AuthenticationController {
    private final AccountRepository accountRepository;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) {
        Optional<Account> accountOptional=accountRepository.findByUsername(loginDto.username());
        if (accountOptional.isEmpty()) return ResponseEntity.status(400).body("Invalid username");
        Account account=accountOptional.get();
        if (!account.getPassword().equals(loginDto.password())) {
            return ResponseEntity.status(400).body("Invalid password");
        }
        if (account.getUserRole()==null) return ResponseEntity.status(400).body("Invalid user role");
        UserRole role=account.getUserRole();
        var accessToken=jwtUtils.generateAccessToken(account.getId(), role);
        var refreshToken=jwtUtils.generateRefreshToken(account.getId(), role);
        return ResponseEntity.ok(new ResponseLoginDto(accessToken, refreshToken, "Access granted successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenDto refreshToken) {
        int accountId;
        try {
            accountId=Integer.parseInt(jwtUtils.validateAndExtractIdRefreshToken(refreshToken.refreshToken()));
        }
        catch (JwtException e) {
            return ResponseEntity.status(400).body("Invalid refresh token");
        }
        Optional<Account> accountOptional=accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) return ResponseEntity.status(400).body("Account not found");
        var newToken=jwtUtils.generateAccessToken(accountId, accountOptional.get().getUserRole());
        return ResponseEntity.ok().body(new RefreshResultDto(newToken));
    }
}
