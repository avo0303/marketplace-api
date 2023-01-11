package com.andrewsha.marketplace.security;

import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtils {
    @Value("${app.security.jwt.secret}")
    private String jwtSecret;
    @Value("${app.security.jwt.accessToken.expired}")
    private String accessTokenExpired;
    @Value("${app.security.jwt.refreshToken.expired}")
    private String refreshTokenExpired;
    @Value("${app.security.jwt.cookie.accessToken.name}")
    private String accessTokenName;
    @Value("${app.security.jwt.cookie.refreshToken.name}")
    private String refreshTokenName;
    @Value("${app.security.jwt.cookie.expiry}")
    private String jwtCookieExpiry;

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(this.jwtSecret.getBytes());
    }

    public JwtToken getAccessToken(HttpServletRequest request) {
        Cookie accessTokenCookie = WebUtils.getCookie(request, this.accessTokenName);
        if (accessTokenCookie != null) {
            return new JwtToken(this.accessTokenName, accessTokenCookie.getValue());
        }
        return null;
    }

    public JwtToken getRefreshToken(HttpServletRequest request) {
        Cookie refreshTokenCookie = WebUtils.getCookie(request, this.refreshTokenName);
        if (refreshTokenCookie != null) {
            return new JwtToken(this.refreshTokenName, refreshTokenCookie.getValue());
        }
        return null;
    }

    public DecodedJWT validateJwt(JwtToken token) throws TokenExpiredException {
        try {
            JWTVerifier jwtVerifier = JWT.require(this.getAlgorithm()).build();
            return jwtVerifier.verify(token.getValue());
        } catch (TokenExpiredException e) {
            return null;
        }
    }

    public JwtToken generateAccessToken(UserDetails userDetails, HttpServletRequest request) {
        String accessTokenValue = JWT.create().withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(
                        System.currentTimeMillis() + Long.valueOf(this.accessTokenExpired)))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(this.getAlgorithm());
        return new JwtToken(this.accessTokenName, accessTokenValue);
    }

    public JwtToken generateRefreshToken(UserDetails userDetails, HttpServletRequest request) {
        String refreshToken = JWT.create().withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(
                        System.currentTimeMillis() + Long.valueOf(this.refreshTokenExpired)))
                .withIssuer(request.getRequestURL().toString()).sign(this.getAlgorithm());
        return new JwtToken(this.refreshTokenName, refreshToken);
    }

    public ResponseCookie createTokenCookie(JwtToken token) {
        return ResponseCookie.from(token.getName(), token.getValue()).domain("").httpOnly(true)
                .path("")
                // .secure(true)
                .sameSite("Strict").maxAge(Long.parseLong(this.jwtCookieExpiry)).build();
    }
}
