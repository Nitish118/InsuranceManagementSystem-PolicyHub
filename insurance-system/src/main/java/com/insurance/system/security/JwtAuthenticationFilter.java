package com.insurance.system.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        
        // Log the request details
        String requestURL = request.getRequestURL().toString();
        String requestMethod = request.getMethod();
        
        // Only log important endpoints to reduce noise
        boolean isImportantUrl = requestURL.contains("/api/claims") || 
                                 requestURL.contains("/api/admin") || 
                                 requestURL.contains("/api/users");
        
        if (isImportantUrl) {
            System.out.println("\n===== REQUEST INFO =====");
            System.out.println("Request URL: " + requestURL);
            System.out.println("Request Method: " + requestMethod);
            System.out.println("Auth header present: " + (authHeader != null));
        }
        
        String username = null;
        String jwt = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                
                if (isImportantUrl) {
                    System.out.println("Token received: " + jwt.substring(0, Math.min(10, jwt.length())) + "...");
                    System.out.println("Extracted username: " + username);
                
                    // Also log the role from the token
                    String role = jwtUtil.extractRole(jwt);
                    System.out.println("Extracted role from token: " + role);
                }
                
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                        // Create authentication object with role from token
                        // Ensure the role has the ROLE_ prefix
                        String role = jwtUtil.extractRole(jwt);
                        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
                        
                        if (isImportantUrl) {
                            System.out.println("Creating authority: " + grantedAuthority.getAuthority());
                        }
                        
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                Collections.singletonList(grantedAuthority));
                        usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        
                        if (isImportantUrl) {
                            System.out.println("Authentication set in context with authority: " + grantedAuthority.getAuthority());
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error processing JWT token: " + e.getMessage());
                if (isImportantUrl) {
                    e.printStackTrace();
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
}