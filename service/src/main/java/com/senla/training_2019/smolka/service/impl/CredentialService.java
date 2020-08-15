package com.senla.training_2019.smolka.service.impl;

import com.senla.training_2019.smolka.api.dao.ICredentialDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.ICredentialService;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.model.entities.Credential;
import com.senla.training_2019.smolka.model.entities.Credential_;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;


@Service
public class CredentialService implements ICredentialService, UserDetailsService {

    private static final Logger logger = LogManager.getLogger(CredentialService.class);
    private static final String USERNAME_NOT_FOUND_FORMATTED_STRING = "Username with login %s doesn't exists!";
    private final ICredentialDao credentialDao;

    @Autowired
    public CredentialService(ICredentialDao credentialDao) {
        this.credentialDao = credentialDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Credential findCredentialByUserId(Long id) throws InternalServiceException {
        try {
            return credentialDao.findByUserId(id);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(Credential_.username, s);
            Credential creds = credentialDao.findByAttribute(EntityGraphType.EXTENDED, null, filterSettings);
            if (creds == null) {
                Formatter formatter = new Formatter();
                formatter.format(USERNAME_NOT_FOUND_FORMATTED_STRING, s);
                throw new UsernameNotFoundException(formatter.toString());
            }
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(creds.getRole().getStringValWithRolePrefix()));
            return new User(creds.getUsername(), creds.getPassword(), authorities);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new AuthorizationServiceException(dataAccessException.getMessage());
        }
    }
}
