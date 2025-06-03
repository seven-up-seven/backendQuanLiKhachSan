package com.example.backendqlks.service;

import com.example.backendqlks.dao.AccountRepository;
import com.example.backendqlks.dto.authentication.LoginDto;
import com.example.backendqlks.dto.authentication.ResponseLoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AuthenticationService {
    private final AccountRepository accountRepository;
//    TODO: private final JwtTokenService jwtTokenService;
    public AuthenticationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String authenticate(LoginDto loginDto){
        var account = accountRepository.findByUsername((loginDto.getUsername()));
        if(account.isPresent() && account.get().getPassword().equals(loginDto.getPassword())) {
//            return jwtTokenService.generateToken(account.get().getUsername());
        }
        return null;
    }
}
