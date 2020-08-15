package com.senla.training_2019.smolka.dao.impl;

import com.senla.training_2019.smolka.api.dao.ICityDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.model.entities.City_;
import com.senla.training_2019.smolka.model.entities.City;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Repository
public class CityDao extends ADao<City, Integer> implements ICityDao {

    @Override
    protected EntityGraph<City> createExtendedEntityGraph() {
        EntityGraph<City> entityGraph = super.createExtendedEntityGraph();
        entityGraph.addAttributeNodes(City_.country.getName());
        return entityGraph;
    }

    @Override
    @Transactional
    public City update(City entity) throws DataAccessException {
        try {
            Query query = entityManager.createNativeQuery("UPDATE city SET city_name = ?, country_id = ? WHERE id = ?");
            query.setParameter(1, entity.getCityName());
            query.setParameter(2, entity.getCountry().getId());
            query.setParameter(3, entity.getId());
            query.executeUpdate();
            return entity;
        }
        catch (RuntimeException runtimeException) {
            throw new DataAccessException(runtimeException.getMessage());
        }
    }
}
