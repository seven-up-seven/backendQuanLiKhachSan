package com.example.backendqlks.utils;

import com.example.backendqlks.entity.UserRole;

public record JwtAccountInfo(
   int accountId,
   UserRole role
) {}
