package com.senla.training_2019.smolka.api.service;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.model.dto.extended.PositionExtendedDto;
import com.senla.training_2019.smolka.model.dto.update_dto.PositionChangeDto;
import com.senla.training_2019.smolka.model.dto.simple.PositionSimpleDto;
import com.senla.training_2019.smolka.model.entities.Position;

import java.util.List;

public interface IPositionService {
    Position createPosition(PositionChangeDto position) throws InternalServiceException, EntityNotFoundException;
    Position updatePosition(PositionChangeDto position) throws InternalServiceException, EntityNotFoundException;
    void deletePosition(Long id) throws InternalServiceException, EntityNotFoundException;
    PositionExtendedDto findPositionById(Long id) throws InternalServiceException, EntityNotFoundException;

    List<PositionSimpleDto> findPositionsWithCurrentCosts(Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<PositionSimpleDto> findPositionsWithCurrentCostsByStoreId(Long storeId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<PositionSimpleDto> findPositionsWithCurrentCostsByProductId(Long productId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<PositionSimpleDto> findPositionsWithCurrentCostsByProductIdAndStoreId(Long productId, Long storeId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;

    Long getCountForPositions() throws InternalServiceException;
    Long getCountForPositionsByStoreId(Long storeId) throws InternalServiceException;
    Long getCountForPositionsByStoreIdAndProductId(Long storeId, Long productId) throws InternalServiceException;
    Long getCountForPositionsByProductId(Long productId) throws InternalServiceException;
}
