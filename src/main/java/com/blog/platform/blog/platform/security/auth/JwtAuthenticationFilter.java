package com.blog.platform.blog.platform.security.auth;

import com.blog.platform.blog.platform.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This is a custom filter that runs once for every incoming request.
 * Its job is to check for a JWT token in the request header, validate it,
 * and set the user's authentication details in the Spring Security context.
 */
@Component
@RequiredArgsConstructor // Creates a constructor with all final fields
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // Our CustomUserDetailsService

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Get the "Authorization" header from the request.
        final String authHeader = request.getHeader("Authorization");

        // 2. Check if the header is missing or doesn't start with "Bearer ".
        // If it is, we pass the request to the next filter in the chain and stop here.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract the JWT token from the "Bearer " prefix.
        final String jwt = authHeader.substring(7); // "Bearer ".length() is 7

        // 4. Extract the username from the token using our JwtService.
        final String username = jwtService.extractUsername(jwt);

        // 5. Check if we have a username AND the user is not already authenticated.
        // The second check prevents us from re-doing this work for every filter in the chain.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Load the user's details from the database using our UserDetailsService.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 7. Check if the token is valid for this user.
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 8. If the token is valid, create an authentication token.
                // This is the object that Spring Security uses to represent the current user.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // We don't need credentials (password) here
                        userDetails.getAuthorities()
                );

                // 9. Set some extra details about the request (like IP address, session ID).
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 10. Update the SecurityContextHolder with the new authentication token.
                // From this point on, the user is considered authenticated for this request.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 11. Pass the request to the next filter in the chain.
        filterChain.doFilter(request, response);
    }
}
