package com.senla.training_2019.smolka.api.dao;

import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.model.entities.Credential;

public interface ICredentialDao extends IDao<Credential, Long> {

    Credential findByUserId(Long id) throws DataAccessException;
}
