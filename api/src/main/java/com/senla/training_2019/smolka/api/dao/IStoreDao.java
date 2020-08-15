package com.senla.training_2019.smolka.api.dao;

import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.model.entities.Store;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;

import java.util.List;

public interface IStoreDao extends IDao<Store, Long> {
    List<Store> findStoresByIdArray(EntityGraphType entityGraphType, Long... storeIdArr) throws DataAccessException;
}
