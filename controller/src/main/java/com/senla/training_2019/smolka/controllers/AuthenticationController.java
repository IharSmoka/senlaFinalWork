package com.senla.training_2019.smolka.controllers;

import com.senla.training_2019.smolka.config.file_config.FileConfig;
import com.senla.training_2019.smolka.config.secutiry.api.ITokenParser;
import com.senla.training_2019.smolka.model.dto.LoginCredentialDto;
import com.senla.training_2019.smolka.model.dto.MessageDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthenticationController {

    private static final String LOGOUT_IS_SUCCESS = "Logout is success!";
    private final AuthenticationManager authenticationManager;
    private final String AUTH_HEADER;
    private final ITokenParser tokenParser;

    public AuthenticationController(AuthenticationManager authenticationManager, FileConfig fileConfig, ITokenParser tokenParser) {
        this.authenticationManager = authenticationManager;
        this.AUTH_HEADER = fileConfig.getTokenHeader();
        this.tokenParser = tokenParser;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Authentication> login(@RequestBody LoginCredentialDto loginCredentialDto) {
        HttpHeaders responseHeaders = new HttpHeaders();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginCredentialDto.getUsername(), loginCredentialDto.getPassword()));
        responseHeaders.set(AUTH_HEADER, tokenParser.createToken(authentication));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(authentication);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<MessageDto> logout() {
        HttpHeaders responseHeaders = new HttpHeaders();
        SecurityContextHolder.getContext().setAuthentication(null);
        MessageDto messageDto = new MessageDto(LOGOUT_IS_SUCCESS);
        responseHeaders.set(AUTH_HEADER, null);
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(messageDto);
    }
}
