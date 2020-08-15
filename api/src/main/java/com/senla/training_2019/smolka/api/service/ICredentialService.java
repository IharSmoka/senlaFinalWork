package com.senla.training_2019.smolka.api.service;

import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.model.entities.Credential;

public interface ICredentialService {
    Credential findCredentialByUserId(Long id) throws InternalServiceException;
}
