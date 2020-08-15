package com.senla.training_2019.smolka.api.service;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.model.dto.update_dto.CostDynamicChangeDto;
import com.senla.training_2019.smolka.model.dto.info_dto.StoreCostDifferenceDto;
import com.senla.training_2019.smolka.model.dto.CostDynamicDto;
import com.senla.training_2019.smolka.model.entities.CostDynamic;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ICostDynamicService {
    CostDynamic createCost(CostDynamicChangeDto costDynamic) throws InternalServiceException, EntityNotFoundException;
    CostDynamic updateCost(CostDynamicChangeDto costDynamic) throws InternalServiceException, EntityNotFoundException;
    void createCostsFromCsvText(String text) throws InternalServiceException, EntityNotFoundException;
    void deleteCost(Long id) throws InternalServiceException, EntityNotFoundException;
    CostDynamicDto findCostById(Long id) throws InternalServiceException, EntityNotFoundException;
    List<CostDynamicDto> findCostDynamicsByPositionId(Long positionId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException, EntityNotFoundException;
    StoreCostDifferenceDto getDifferenceBetweenCostAtStores(Long storeId1, Long storeId2) throws InternalServiceException, EntityNotFoundException;
    Map<String, String> getDynamicForProductAndStoreBetweenDates(Long productId, Long storeId, Date date1, Date date2) throws InternalServiceException, EntityNotFoundException;
}
