package org.vrr.simplecloudservice.security.jwt.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vrr.simplecloudservice.properties.JwtProperties;
import org.vrr.simplecloudservice.security.LogoutService;
import org.vrr.simplecloudservice.security.jwt.JwtUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final static String INVALID_JWT_BEARER = "Invalid JWT in bearer header";

    private final static String INVALID_JWT_TOKEN = "Invalid JWT token";

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    private final LogoutService logoutService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(jwtProperties.getAuthorizationHeader());
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(jwtProperties.getBearerName())){
            String jwt = authHeader.substring(jwtProperties.getBearerLength());

            if (jwt.isBlank()){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, INVALID_JWT_BEARER);
            } else {
                if (logoutService.checkLogoutExistence(authHeader)){
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, INVALID_JWT_BEARER);
                    log.info("Not valid token (logout)");
                    return;
                }
                try {
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null){
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException exc){
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            INVALID_JWT_TOKEN);
                }
            }

        }
        filterChain.doFilter(request, response);
    }
}
