package com.andrewsha.marketplace.security.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.andrewsha.marketplace.exception.JwtUtilsException;
import com.andrewsha.marketplace.security.JwtToken;
import com.andrewsha.marketplace.security.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class TokenFilter extends OncePerRequestFilter {
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtUtils jwtUtils;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		try {
			JwtToken accessToken = this.jwtUtils.getAccessToken(request);
			if (accessToken != null) {
				UserDetails userDetails;
				DecodedJWT decodedAccessToken = this.jwtUtils.validateJwt(accessToken);
				if (decodedAccessToken != null) {
					String username = decodedAccessToken.getSubject();
					userDetails = userDetailsService.loadUserByUsername(username);
				} else {
					JwtToken refreshToken = this.jwtUtils.getRefreshToken(request);
					if (refreshToken != null) {
						DecodedJWT decodedRefreshToken = this.jwtUtils.validateJwt(refreshToken);
						String username = decodedRefreshToken.getSubject();
						userDetails = userDetailsService.loadUserByUsername(username);
						accessToken = this.jwtUtils.generateAccessToken(userDetails, request);

						response.setHeader(HttpHeaders.SET_COOKIE,
								this.jwtUtils.createTokenCookie(accessToken).toString());
					} else {
						throw new JwtUtilsException("cannot refresh token");
					}
				}
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(userDetails, null,
								userDetails.getAuthorities());
				authenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContext context = SecurityContextHolder.createEmptyContext();
				context.setAuthentication(authenticationToken);
				SecurityContextHolder.setContext(context);
			}
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
		}
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200");
		filterChain.doFilter(request, response);
	}
}
