package com.senla.training_2019.smolka.config.secutiry;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.training_2019.smolka.config.secutiry.api.ITokenParser;
import com.senla.training_2019.smolka.model.dto.LoginCredentialDto;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class TokenParser implements ITokenParser {

    private final UserDetailsService userDetailsService;
    private final String secretKey;

    public TokenParser(UserDetailsService userDetailsService, String secretKey) {
        this.userDetailsService = userDetailsService;
        this.secretKey = secretKey;
    }

    @Override
    public String createToken(Authentication authentication) {
        UserDetails user = ((UserDetails) authentication.getPrincipal());
        return JWT.create()
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withSubject("{\"username\":\"" + user.getUsername() + "\"" + "}")
                .withClaim("\"role\":\"", user.getAuthorities().toString())
                .sign(HMAC512(secretKey.getBytes()));
    }

    @Override
    public UserDetails parseToken(String token) {
        try {
            final String decodedToken = JWT.require(HMAC512(secretKey.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();
            return userDetailsService.loadUserByUsername(new ObjectMapper().readValue(decodedToken, LoginCredentialDto.class).getUsername());
        }
        catch (JWTDecodeException | SignatureVerificationException | JsonProcessingException exc) {
            throw new AuthorizationServiceException(exc.getMessage());
        }
    }
}
