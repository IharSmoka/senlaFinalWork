package com.senla.training_2019.smolka.api.dao;

import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.model.entities.CostDynamic;
import com.senla.training_2019.smolka.model.entities.Position;
import com.senla.training_2019.smolka.model.entities.Product;
import com.senla.training_2019.smolka.model.entities.Store;

import java.util.Date;
import java.util.List;

public interface ICostDynamicDao extends IDao<CostDynamic, Long> {
    List<CostDynamic> findByPositionsBetweenDates(EntityGraphType entityGraphType, Date date1, Date date2, Position... positions) throws DataAccessException;
    CostDynamic findCurrentCostByPosition(EntityGraphType entityGraphType, Position position) throws DataAccessException;
    List<CostDynamic> findByProductAndStoreBetweenDates(EntityGraphType entityGraphType, Product product, Store store, Date date1, Date date2) throws DataAccessException;
    List<CostDynamic> findByProduct(EntityGraphType entityGraphType, Product product) throws DataAccessException;
    Long findCurrentCostByProductAtStore(Product product, Store store) throws DataAccessException;
}
