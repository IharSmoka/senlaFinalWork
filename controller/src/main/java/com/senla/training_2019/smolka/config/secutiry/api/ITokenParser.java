package com.senla.training_2019.smolka.config.secutiry.api;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface ITokenParser {
    String createToken(Authentication authentication);
    UserDetails parseToken(String token);
}
