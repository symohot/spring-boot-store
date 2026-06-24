package com.codewithmosh.store.filters;

import com.codewithmosh.store.entities.Role;
import com.codewithmosh.store.services.Jwt;
import com.codewithmosh.store.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.replace("Bearer ", "");

        /*if (!jwtService.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }*/
        Jwt jwt = jwtService.parseToken(token);
        if (jwt == null || jwt.isExpired()) {
            filterChain.doFilter(request, response);
            return;
        }

        /*Long userId = jwtService.getUserId(token);
        Role role = jwtService.getRole(token);*/

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                jwt.getUserId(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_"+ jwt.getRole()))
        );
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
