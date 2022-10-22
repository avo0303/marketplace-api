package com.andrewsha.marketplace.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.andrewsha.marketplace.security.JwtToken;
import com.andrewsha.marketplace.security.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        JwtToken accessToken = this.jwtUtils
                .generateAccessToken((UserDetails) authentication.getPrincipal(), request);
        JwtToken refreshToken = this.jwtUtils
                .generateRefreshToken((UserDetails) authentication.getPrincipal(), request);
        response.setHeader(HttpHeaders.SET_COOKIE,
                this.jwtUtils.createTokenCookie(accessToken).toString());
        response.addHeader(HttpHeaders.SET_COOKIE,
                this.jwtUtils.createTokenCookie(refreshToken).toString());
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), authentication.getPrincipal());
    }
}
