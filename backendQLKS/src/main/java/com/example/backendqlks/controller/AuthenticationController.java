package com.example.backendqlks.controller;

import com.example.backendqlks.dao.AccountRepository;
import com.example.backendqlks.dao.GuestRepository;
import com.example.backendqlks.dao.StaffRepository;
import com.example.backendqlks.dto.account.ChangePasswordDto;
import com.example.backendqlks.dto.authentication.*;
import com.example.backendqlks.entity.Account;
import com.example.backendqlks.entity.UserRole;
import com.example.backendqlks.service.AccountService;
import com.example.backendqlks.service.HistoryService;
import com.example.backendqlks.service.SMTPEmailService;
import com.example.backendqlks.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final HistoryService historyService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) {
        Optional<Account> accountOptional = accountRepository.findByUsername(loginDto.getUsername());
        if (accountOptional.isEmpty()) return ResponseEntity.status(400).body("Invalid username");
        Account account = accountOptional.get();
        if (!account.getPassword().equals(loginDto.getPassword())) {
            return ResponseEntity.status(400).body("Invalid password");
        }
        if (account.getUserRole() == null) return ResponseEntity.status(400).body("Invalid user role");
        UserRole role = account.getUserRole();
        var accessToken = jwtUtils.generateAccessToken(account.getId(), role);
        var refreshToken = jwtUtils.generateRefreshToken(account.getId(), role);
        return ResponseEntity.ok(new ResponseLoginDto(accessToken, refreshToken, "Access granted successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenDto refreshToken) {
        int accountId;
        try {
            accountId = Integer.parseInt(jwtUtils.validateAndExtractIdRefreshToken(refreshToken.getRefreshToken()));
        } catch (JwtException e) {
            return ResponseEntity.status(400).body("Invalid refresh token");
        }
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) return ResponseEntity.status(400).body("Account not found");
        var newToken = jwtUtils.generateAccessToken(accountId, accountOptional.get().getUserRole());
        return ResponseEntity.ok().body(new RefreshResultDto(newToken));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDto request) {
        Optional<Account> accountOpt = accountRepository.findByUsername(request.getUsername());

        if (accountOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Username không tồn tại");
        }

        Account account = accountOpt.get();

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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPTIONIST', 'MANAGER', 'GUEST', 'ACCOUNTANT')")
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("User ID extracted from token: {}", userIdStr);
        try {
            int userId = Integer.parseInt(userIdStr); // Chuyển đổi sang int
            Optional<Account> accountOptional = accountRepository.findById(userId);
            if (accountOptional.isEmpty()) {
                logger.warn("No account found for userId: {}", userId);
                return ResponseEntity.badRequest().body("Tài khoản không tồn tại.");
            }
            Account account = accountOptional.get();

            // Kiểm tra mật khẩu cũ
            if (!account.getPassword().equals(dto.getOldPassword())) {
                return ResponseEntity.badRequest().body("Mật khẩu cũ không đúng.");
            }

            // Kiểm tra mật khẩu mới và xác nhận
            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Mật khẩu xác nhận không khớp.");
            }

            // Cập nhật mật khẩu
            account.setPassword(dto.getNewPassword());
            accountRepository.save(account);

            // Lưu lịch sử
            historyService.saveHistory(
                    "GUEST",
                    "ACCOUNT",
                    account.getId(),
                    account.getId(),
                    "UPDATE",
                    "Thay đổi mật khẩu"
            );

            return ResponseEntity.ok("Thay đổi mật khẩu thành công.");
        } catch (NumberFormatException e) {
            logger.error("Invalid user ID format: {}", userIdStr);
            return ResponseEntity.badRequest().body("Dữ liệu không hợp lệ.");
        }
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