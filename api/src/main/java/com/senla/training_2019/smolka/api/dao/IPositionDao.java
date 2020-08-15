package com.senla.training_2019.smolka.api.dao;

import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.model.entities.Position;
import com.senla.training_2019.smolka.model.entities.Store;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;
import com.senla.training_2019.smolka.utils.pagination_settings.PaginationSettings;

import java.util.List;
import java.util.Map;

public interface IPositionDao extends IDao<Position, Long> {
    List<Position> findPositionsWithEqualProductsAtStores(EntityGraphType entityGraphType, Long... storesId) throws DataAccessException;
    List<Position> findCurrentCostsForPositions(EntityGraphType entityGraphType, Long... positionsId) throws DataAccessException;
}
