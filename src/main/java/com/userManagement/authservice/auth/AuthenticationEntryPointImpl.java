package com.userManagement.authservice.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
 
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

//        log.error("Responding with unauthorized error. Message - {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}