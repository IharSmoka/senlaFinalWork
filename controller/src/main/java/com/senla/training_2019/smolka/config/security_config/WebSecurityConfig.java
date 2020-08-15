package com.senla.training_2019.smolka.config.security_config;

import com.senla.training_2019.smolka.config.file_config.FileConfig;
import com.senla.training_2019.smolka.config.secutiry.AuthorizationFilter;
import com.senla.training_2019.smolka.config.secutiry.TokenParser;
import com.senla.training_2019.smolka.config.secutiry.api.ITokenParser;
import com.senla.training_2019.smolka.model.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final FileConfig fileConfig;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService, FileConfig fileConfig) {
        this.userDetailsService = userDetailsService;
        this.fileConfig = fileConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login", "/users").anonymous()
                .antMatchers(HttpMethod.POST, "/countries", "/addresses", "/categories", "/cities", "/costs", "/positions", "/makers", "/products", "/stores").hasAnyRole(UserRole.MODERATOR.toString(), UserRole.ADMIN.toString())
                .antMatchers(HttpMethod.PUT, "/countries", "/addresses", "/categories", "/cities", "/costs", "/positions", "/makers", "/products", "/stores").hasAnyRole(UserRole.MODERATOR.toString(), UserRole.ADMIN.toString())
                .antMatchers(HttpMethod.DELETE, "/countries", "/users", "/addresses", "/categories", "/cities", "/costs", "/positions", "/makers", "/products", "/stores").hasAnyRole(UserRole.MODERATOR.toString(), UserRole.ADMIN.toString())
                .antMatchers(HttpMethod.PUT, "/users/moderator").hasRole(UserRole.ADMIN.toString());
        http.authorizeRequests()
                .anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilter(authorizationFilter());
    }

    @Bean
    public AuthorizationFilter authorizationFilter() {
        return new AuthorizationFilter(authenticationManager(), fileConfig.getTokenHeader(), tokenParser());
    }

    @Bean
    public ITokenParser tokenParser() {
        return new TokenParser(userDetailsService, fileConfig.getSecretKey());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(getPasswordEncoder());
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
