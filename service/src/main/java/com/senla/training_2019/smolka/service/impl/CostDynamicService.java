package com.senla.training_2019.smolka.service.impl;

import com.senla.training_2019.smolka.api.dao.ICostDynamicDao;
import com.senla.training_2019.smolka.api.dao.IPositionDao;
import com.senla.training_2019.smolka.api.dao.IProductDao;
import com.senla.training_2019.smolka.api.dao.IStoreDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.ICostDynamicService;
import com.senla.training_2019.smolka.mappers.DtoMapper;
import com.senla.training_2019.smolka.model.dto.update_dto.CostDynamicChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.StoreExtendedDto;
import com.senla.training_2019.smolka.model.dto.info_dto.StoreCostDifferenceDto;
import com.senla.training_2019.smolka.model.dto.CostDynamicDto;
import com.senla.training_2019.smolka.model.dto.simple.ProductSimpleDto;
import com.senla.training_2019.smolka.model.entities.*;
import com.senla.training_2019.smolka.price_monitoring_utils.csv_parser.CsvParserException;
import com.senla.training_2019.smolka.price_monitoring_utils.csv_parser.PriceMonitoringUtils;
import com.senla.training_2019.smolka.price_monitoring_utils.sort_utils.PriceMonitoringPaginationSettingsBuilder;
import com.senla.training_2019.smolka.service.impl.exception_messages.EntityNotFoundExceptionMessages;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CostDynamicService implements ICostDynamicService {

    private static final Logger logger = LogManager.getLogger(CostDynamicService.class);
    private static final String STORE_CANNOT_BE_NULL = "Store cannot be null!";
    private final ICostDynamicDao costDynamicDao;
    private final IPositionDao positionDao;
    private final IProductDao productDao;
    private final IStoreDao storeDao;
    private final DtoMapper dtoMapper;

    @Autowired
    public CostDynamicService(ICostDynamicDao costDynamicDao, IPositionDao positionDao, IProductDao productDao, IStoreDao storeDao, DtoMapper dtoMapper) {
        this.costDynamicDao = costDynamicDao;
        this.positionDao = positionDao;
        this.productDao = productDao;
        this.storeDao = storeDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CostDynamic createCost(CostDynamicChangeDto costDynamicDto) throws InternalServiceException, EntityNotFoundException {
        try {
            CostDynamic costDynamic = dtoMapper.mapping(costDynamicDto, CostDynamic.class);
            Position position = positionDao.findById(EntityGraphType.SIMPLE, costDynamicDto.getPositionId());
            if (position == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.POSITION_NOT_EXIST.getMsg());
            }
            costDynamic.setPosition(position);
            return costDynamicDao.save(costDynamic);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public CostDynamic updateCost(CostDynamicChangeDto costDynamicDto) throws InternalServiceException, EntityNotFoundException {
        try {
            CostDynamic costDynamic = costDynamicDao.findById(EntityGraphType.EXTENDED, costDynamicDto.getId());
            if (costDynamic == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.COST_NOT_EXIST.getMsg());
            }
            CostDynamic updCostDynamic = dtoMapper.mapping(costDynamicDto, CostDynamic.class);
            updCostDynamic.setPosition(costDynamic.getPosition());
            return costDynamicDao.update(updCostDynamic);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createCostsFromCsvText(String text) throws InternalServiceException, EntityNotFoundException {
        try {
            List<CostDynamicChangeDto> costDynamicUpdateDtoList = PriceMonitoringUtils.getCostDynamicDtoListFromCsvText(text);
            for (CostDynamicChangeDto costDynamicDto : costDynamicUpdateDtoList) {
                CostDynamic costDynamic = dtoMapper.mapping(costDynamicDto, CostDynamic.class);
                Position position = positionDao.findById(EntityGraphType.SIMPLE, costDynamicDto.getPositionId());
                if (position == null) {
                    throw new EntityNotFoundException(EntityNotFoundExceptionMessages.POSITION_NOT_EXIST.getMsg());
                }
                costDynamic.setPosition(position);
                costDynamicDao.save(costDynamic);
            }
        }
        catch (DataAccessException | CsvParserException exc) {
            logger.error(exc.getMessage(), exc);
            throw new InternalServiceException(exc.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteCost(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            CostDynamic costDynamic = costDynamicDao.findById(EntityGraphType.EXTENDED, id);
            if (costDynamic == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.COST_NOT_EXIST.getMsg());
            }
            Position position = costDynamic.getPosition();
            costDynamicDao.delete(id);
            Long countOfDynamics = costDynamicDao.getCountByFilterSettings(new SingularAttributeFilterSettings().putToSettingsWithEqualInstruction(CostDynamic_.position, position));
            if (countOfDynamics == 0L) {
                positionDao.delete(position.getId());
            }
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CostDynamicDto findCostById(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            CostDynamic costDynamic = costDynamicDao.findById(EntityGraphType.SIMPLE, id);
            if (costDynamic == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.COST_NOT_EXIST.getMsg());
            }
            return dtoMapper.mapping(costDynamic, CostDynamicDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CostDynamicDto> findCostDynamicsByPositionId(Long positionId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException, EntityNotFoundException {
        try {
            Position position = positionDao.findById(EntityGraphType.SIMPLE, positionId);
            if (position == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.POSITION_NOT_EXIST.getMsg());
            }
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(CostDynamic_.position, Position_.id, positionId);
            return dtoMapper.toListMapping(costDynamicDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings), CostDynamicDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public StoreCostDifferenceDto getDifferenceBetweenCostAtStores(Long storeId1, Long storeId2) throws InternalServiceException, EntityNotFoundException {
        try {
            if (storeId1 == null || storeId2 == null) {
                throw new IllegalArgumentException(STORE_CANNOT_BE_NULL);
            }
            List<Store> stores = storeDao.findStoresByIdArray(EntityGraphType.EXTENDED, storeId1, storeId2);
            if (stores == null || stores.size() != 2) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.STORE_NOT_EXIST.getMsg());
            }
            List<Position> equalPositions = positionDao.findPositionsWithEqualProductsAtStores(EntityGraphType.SIMPLE, storeId1, storeId2);
            if (equalPositions == null) {
                return null;
            }
            List<Position> positionsWithCurrentCosts = positionDao.findCurrentCostsForPositions(EntityGraphType.EXTENDED, equalPositions.stream().map(Position::getId).collect(Collectors.toList()).toArray(Long[]::new));
            Map<Position, Long> costMapForPositions = positionsWithCurrentCosts.stream().collect(Collectors.toMap(position -> position, position -> position.getCostDynamics().get(0).getCost()));
            Set<Product> exclusionProductsSet = new HashSet<>();
            for (Position position : equalPositions) {
                if (!costMapForPositions.containsKey(position)) {
                    exclusionProductsSet.add(position.getProduct());
                }
            }
            Map<Store, Map<Product, Long>> productsAtStoresCostMap = new HashMap<>();
            Store firstStore = stores.get(0);
            Store secondStore = stores.get(1);
            StoreCostDifferenceDto costDifferenceDto = new StoreCostDifferenceDto(dtoMapper.mapping(firstStore, StoreExtendedDto.class), dtoMapper.mapping(secondStore, StoreExtendedDto.class));
            for (Position position : costMapForPositions.keySet()) {
                if (!productsAtStoresCostMap.containsKey(position.getStore())) {
                    productsAtStoresCostMap.put(position.getStore(), new HashMap<>());
                }
                if (!exclusionProductsSet.contains(position.getProduct())) {
                    Map<Product, Long> productsWithCostMap = productsAtStoresCostMap.get(position.getStore());
                    productsWithCostMap.put(position.getProduct(), costMapForPositions.get(position));
                }
            }
            Map<Product, Long> productsFromFirstStore = productsAtStoresCostMap.get(firstStore);
            for (Product product : productsFromFirstStore.keySet()) {
                ProductSimpleDto productDto = dtoMapper.mapping(product, ProductSimpleDto.class);
                Long diff =  productsAtStoresCostMap.get(firstStore).get(product) - productsAtStoresCostMap.get(secondStore).get(product);
                String strDiff = PriceMonitoringUtils.getCostFromMinorVal(diff);
                costDifferenceDto.addProductCostDifference(productDto, strDiff);
            }
            return costDifferenceDto;
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, String> getDynamicForProductAndStoreBetweenDates(Long productId, Long storeId, Date date1, Date date2) throws InternalServiceException, EntityNotFoundException {
        try {
            Product product = productDao.findById(EntityGraphType.SIMPLE, productId);
            Store store = storeDao.findById(EntityGraphType.SIMPLE, storeId);
            if (product == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.PRODUCT_NOT_EXIST.getMsg());
            }
            if (store == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.STORE_NOT_EXIST.getMsg());
            }
            List<CostDynamic> costDynamicsForProduct = costDynamicDao.findByProductAndStoreBetweenDates(EntityGraphType.SIMPLE, product, store, date1, date2);

            Map<String, String> dateCostDynamicMap = new HashMap<>();
            for (CostDynamic costDynamic : costDynamicsForProduct) {

                dateCostDynamicMap.put(costDynamic.getCostDate().toString(), PriceMonitoringUtils.getCostFromMinorVal(costDynamic.getCost()));
            }
            return dateCostDynamicMap;
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }
}
