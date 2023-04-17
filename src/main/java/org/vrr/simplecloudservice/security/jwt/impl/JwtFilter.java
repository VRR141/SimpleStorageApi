package org.vrr.simplecloudservice.security.jwt.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vrr.simplecloudservice.security.AuthProvider;
import org.vrr.simplecloudservice.security.LogoutService;
import org.vrr.simplecloudservice.security.jwt.JwtUtil;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@PropertySource(value = "classpath:jwt.properties")
public class JwtFilter extends OncePerRequestFilter {

    //TODO move properties to class

    private final static String INVALID_JWT_BEARER = "Invalid JWT in bearer header";

    private final static String INVALID_JWT_TOKEN = "Invalid JWT token";

    @Value("${jwt.filter.bearer.name}")
    private String bearer;

    @Value("${jwt.filter.bearer.length}")
    private Integer length;

    @Value("${jwt.filter.header.authorization}")
    private String authorization;

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    private final LogoutService logoutService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(authorization);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(bearer)){
            String jwt = authHeader.substring(length);

            if (jwt.isBlank()){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, INVALID_JWT_BEARER);
            } else {
                if (logoutService.checkLogoutExistence(jwtUtil.getUuidFromToken(jwt), authHeader)){
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, INVALID_JWT_BEARER);
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
