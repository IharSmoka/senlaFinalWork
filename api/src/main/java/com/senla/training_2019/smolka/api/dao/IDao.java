package com.senla.training_2019.smolka.api.dao;

import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;
import com.senla.training_2019.smolka.utils.pagination_settings.PaginationSettings;

import java.util.List;

public interface IDao<E, Id> {
    E save(E entity) throws DataAccessException;
    void delete(Id id) throws DataAccessException;
    E update(E entity) throws DataAccessException;
    E findById(EntityGraphType graphType, Id id) throws DataAccessException;
    E findByAttribute(EntityGraphType graphType, PaginationSettings paginationSettings, SingularAttributeFilterSettings singularAttributeFilterSettings) throws DataAccessException;
    List<E> findAll(EntityGraphType graphType, PaginationSettings paginationSettings) throws DataAccessException;
    List<E> findAllWithFilterSettings(EntityGraphType graphType, PaginationSettings paginationSettings, SingularAttributeFilterSettings singularAttributeFilterSettings) throws DataAccessException;
    Long getCount() throws DataAccessException;
    Long getCountByFilterSettings(SingularAttributeFilterSettings filterSettings) throws DataAccessException;
}
