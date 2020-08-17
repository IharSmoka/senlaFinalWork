package com.senla.training_2019.smolka.dao.impl;

import com.senla.training_2019.smolka.api.dao.ICountryDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.model.entities.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
//import javax.transaction.Transactional;

@Repository
public class CountryDao extends ADao<Country, Integer> implements ICountryDao {

    private final JdbcTemplate template;

    @Autowired
    public CountryDao(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    @Transactional
    public void delete(Integer id) throws DataAccessException {
        try {
            Query query = entityManager.createQuery("DELETE FROM Country c WHERE c.id = ?1");
            query.setParameter(1, id);
            query.executeUpdate();
        }
        catch (RuntimeException runtimeException) {
            throw new DataAccessException(runtimeException.getMessage());
        }
    }

    @Override
    @Transactional
    public Country update(Country entity) throws DataAccessException {
        try {
            template.update("UPDATE country SET country_name = \'" + entity.getCountryName() + "\' WHERE id = "+entity.getId());
            return entity;
        }
        catch (org.springframework.dao.DataAccessException dataAccessException) {
            throw new DataAccessException(dataAccessException.getMessage());
        }
    }
}
