package com.senla.training_2019.smolka.config.secutiry;

import com.senla.training_2019.smolka.config.secutiry.api.ITokenParser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final ITokenParser tokenParser;
    private final String AUTH_HEADER;

    public AuthorizationFilter(AuthenticationManager authenticationManager, String authHeader, ITokenParser tokenParser) {
        super(authenticationManager);
        this.AUTH_HEADER = authHeader;
        this.tokenParser = tokenParser;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(AUTH_HEADER);
        if (token == null || token.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }
        UserDetails userDetails = tokenParser.parseToken(token);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request, response);
    }
}
