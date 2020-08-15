package com.senla.training_2019.smolka.dao.impl;

import com.senla.training_2019.smolka.api.dao.ICredentialDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.model.entities.Credential;
import com.senla.training_2019.smolka.model.entities.UserInfo;
import com.senla.training_2019.smolka.model.entities.UserInfo_;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

@Repository
public class CredentialDao extends ADao<Credential, Long> implements ICredentialDao {

    @Override
    public Credential findByUserId(Long id) throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Credential> query = builder.createQuery(Credential.class);
            Root<UserInfo> root = query.from(UserInfo.class);
            Join<UserInfo, Credential> join = root.join(UserInfo_.credential);
            query.select(join);
            query.where(builder.equal(root.get(UserInfo_.id), id));
            return entityManager.createQuery(query).setMaxResults(1).getSingleResult();
        }
        catch (PersistenceException persistenceException) {
            throw new DataAccessException(persistenceException.getMessage());
        }
    }
}
