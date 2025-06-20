package com.example.backendqlks.controller;

import com.example.backendqlks.dao.AccountRepository;
import com.example.backendqlks.dao.GuestRepository;
import com.example.backendqlks.dao.StaffRepository;
import com.example.backendqlks.dto.authentication.*;
import com.example.backendqlks.entity.Account;
import com.example.backendqlks.entity.UserRole;
import com.example.backendqlks.service.AccountService;
import com.example.backendqlks.service.SMTPEmailService;
import com.example.backendqlks.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authentication")
public class AuthenticationController {
    private final AccountRepository accountRepository;
    private final JwtUtils jwtUtils;
    private final StaffRepository staffRepository;
    private final GuestRepository guestRepository;
    private final SMTPEmailService smtpEmailService;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) {
        Optional<Account> accountOptional=accountRepository.findByUsername(loginDto.getUsername());
        if (accountOptional.isEmpty()) return ResponseEntity.status(400).body("Invalid username");
        Account account=accountOptional.get();
        if (!account.getPassword().equals(loginDto.getPassword())) {
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
            accountId=Integer.parseInt(jwtUtils.validateAndExtractIdRefreshToken(refreshToken.getRefreshToken()));
        }
        catch (JwtException e) {
            return ResponseEntity.status(400).body("Invalid refresh token");
        }
        Optional<Account> accountOptional=accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) return ResponseEntity.status(400).body("Account not found");
        var newToken=jwtUtils.generateAccessToken(accountId, accountOptional.get().getUserRole());
        return ResponseEntity.ok().body(new RefreshResultDto(newToken));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDto request) {
        Optional<Account> accountOpt = accountRepository.findByUsername(request.getUsername());

        if (accountOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Username không tồn tại");
        }

        Account account=accountOpt.get();

        // Ưu tiên tìm trong guest
        var guestOpt = guestRepository.findByAccountId(account.getId());
        if (guestOpt.isPresent()) {
            var guest = guestOpt.get();
            if (guest.getEmail() == null || !guest.getEmail().equalsIgnoreCase(request.getEmail())) {
                return ResponseEntity.badRequest().body("Email không hợp lệ");
            }

            String newPassword = generateSecureRandomPassword(8);
            account.setPassword(newPassword);
            accountRepository.save(account);

            smtpEmailService.sendPasswordReset(guest.getEmail(), newPassword);
            return ResponseEntity.ok("Mật khẩu mới đã được gửi về email.");
        }

        var staffOpt = staffRepository.findByAccountId(account.getId());
        if (staffOpt.isPresent()) {
            var staff = staffOpt.get();
            if (staff.getEmail() == null || !staff.getEmail().equalsIgnoreCase(request.getEmail())) {
                return ResponseEntity.badRequest().body("Email không hợp lệ");
            }

            String newPassword = generateSecureRandomPassword(8);
            account.setPassword(newPassword);
            accountRepository.save(account);

            smtpEmailService.sendPasswordReset(staff.getEmail(), newPassword);
            return ResponseEntity.ok("Mật khẩu mới đã được gửi về email.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng hợp lệ");
    }

    private String generateSecureRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }
}
