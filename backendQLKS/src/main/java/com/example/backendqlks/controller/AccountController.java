package com.example.backendqlks.controller;

import com.example.backendqlks.dto.account.AccountDto;
import com.example.backendqlks.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable int id){
        try{
            return ResponseEntity.ok(accountService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching account: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAccounts(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try{
            return ResponseEntity.ok(accountService.getAll(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching accounts: " + e.getMessage());
        }
    }

    @GetMapping("get-all-no-page")
    public ResponseEntity<?> getAllAccounts() {
        try {
            return ResponseEntity.ok(accountService.getAllNoPage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching accounts: " + e.getMessage());
        }
    }

    @GetMapping("username/{username}")
    public ResponseEntity<?> getAllAccountsByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(accountService.getByUsername(username));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching accounts: " + e.getMessage());
        }
    }

    @GetMapping("user-role-id/{userRoleId}")
    public ResponseEntity<?> getAllAccountsByUserRoleName(@PathVariable Integer userRoleId) {
        try {
            return ResponseEntity.ok(accountService.getByUserRoleId(userRoleId));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching accounts: " + e.getMessage());
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(accountService.get(id));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching account: " + e.getMessage());
        }
    }

    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createAccount(@PathVariable int impactorId,
                                           @PathVariable String impactor,
                                           @Valid @RequestBody AccountDto accountDto,
                                           BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdAccount = accountService.create(accountDto, impactorId, impactor);
            return ResponseEntity.ok(createdAccount);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error creating author: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateAccount(@PathVariable int id,
                                           @PathVariable int impactorId,
                                           @PathVariable String impactor,
                                           @Valid @RequestBody AccountDto accountDto,
                                           BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedAccount = accountService.update(id, accountDto, impactorId, impactor);
            return ResponseEntity.ok(updatedAccount);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error updating account: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteAccount(@PathVariable int id,
                                           @PathVariable int impactorId,
                                           @PathVariable String impactor){
        try{
            accountService.delete(id, impactorId, impactor);
            return ResponseEntity.ok("Account deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting account: " + e.getMessage());
        }
    }
}
