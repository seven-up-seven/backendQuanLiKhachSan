package com.example.backendqlks.controller;

import com.example.backendqlks.dto.account.AccountDto;
import com.example.backendqlks.service.AccountService;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> getAllAccounts(){
        try{
            return ResponseEntity.ok(accountService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching accounts: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@Valid @RequestBody AccountDto accountDto,
                                           BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdAccount = accountService.create(accountDto);
            return ResponseEntity.ok(createdAccount);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error creating author: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable int id,
                                           @Valid @RequestBody AccountDto accountDto,
                                           BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedAccount = accountService.update(id, accountDto);
            return ResponseEntity.ok(updatedAccount);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error updating account: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable int id){
        try{
            accountService.delete(id);
            return ResponseEntity.ok("Account deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting account: " + e.getMessage());
        }
    }
}
