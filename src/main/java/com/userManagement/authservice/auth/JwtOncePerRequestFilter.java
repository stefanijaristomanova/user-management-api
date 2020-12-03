package com.userManagement.authservice.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtOncePerRequestFilter extends OncePerRequestFilter {

    @Value("${authorization.enabled}")
    private boolean authorizationEnabled;

    private final JwtUtils jwtUtils;

    @Autowired
    public JwtOncePerRequestFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if (authorizationEnabled) {
            String token = jwtUtils.resolveToken(request);
            if (!StringUtils.isEmpty(token) && !jwtUtils.isTokenExpired(token) && jwtUtils.validateToken(token)) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(jwtUtils.getUsernameFromToken(token), null,  jwtUtils.getAuthoritiesFromToken(token));
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }
}
