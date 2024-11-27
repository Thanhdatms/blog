package com.group7.blog.configurations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class TokenBinaryFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String base64Token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            try {
                // Decode the Base64 token
                byte[] decodedBytes = Base64.getDecoder().decode(base64Token);
                String decodedJson = new String(decodedBytes);

                // Store the decoded JSON in the request attributes
                request.setAttribute("decodedToken", decodedJson);

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Base64 token: " + base64Token);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
