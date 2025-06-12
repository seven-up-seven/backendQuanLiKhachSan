package com.example.backendqlks.config;

import com.example.backendqlks.service.PermissionService;
import com.example.backendqlks.service.UserRolePermissionService;
import com.example.backendqlks.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserRolePermissionService userRolePermissionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        var token=authorizationHeader.substring(7);
        try {
            var account=jwtUtils.extractAccessToken(token);
            var role=account.role();
            var userRolePermissions=userRolePermissionService.getUserRolePermissionsByRoleId(role.getId());
            List<SimpleGrantedAuthority> authorities = userRolePermissions.stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
                    .toList();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(account.accountId(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            handleUnauthorizedResponse(response, "Invalid JWT token :v");
            return;
        } catch (Exception e) {
            log.error("Error while setting user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private void handleUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        response.getWriter().flush();
        response.getWriter().close();
    }
}
