package com.senla.training_2019.smolka.web.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface ITokenParser {
    String createToken(Authentication authentication);
    UserDetails parseToken(String token);
}
