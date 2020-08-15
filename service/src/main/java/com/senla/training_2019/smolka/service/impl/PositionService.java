package com.senla.training_2019.smolka.service.impl;

import com.senla.training_2019.smolka.api.dao.ICostDynamicDao;
import com.senla.training_2019.smolka.api.dao.IPositionDao;
import com.senla.training_2019.smolka.api.dao.IProductDao;
import com.senla.training_2019.smolka.api.dao.IStoreDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IPositionService;
import com.senla.training_2019.smolka.mappers.DtoMapper;
import com.senla.training_2019.smolka.model.dto.extended.PositionExtendedDto;
import com.senla.training_2019.smolka.model.dto.update_dto.PositionChangeDto;
import com.senla.training_2019.smolka.model.dto.simple.PositionSimpleDto;
import com.senla.training_2019.smolka.model.entities.*;
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

import java.util.List;

@Service
public class PositionService implements IPositionService {

    private static final Logger logger = LogManager.getLogger(PositionService.class);
    private final IPositionDao positionDao;
    private final ICostDynamicDao costDynamicDao;
    private final IStoreDao storeDao;
    private final IProductDao productDao;
    private final DtoMapper dtoMapper;

    @Autowired
    public PositionService(IPositionDao positionDao, ICostDynamicDao costDynamicDao, IStoreDao storeDao, IProductDao productDao, DtoMapper dtoMapper) {
        this.positionDao = positionDao;
        this.costDynamicDao = costDynamicDao;
        this.storeDao = storeDao;
        this.productDao = productDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Position createPosition(PositionChangeDto positionDto) throws InternalServiceException, EntityNotFoundException {
        try {
            Position position = dtoMapper.mapping(positionDto, Position.class);
            Product product = productDao.findById(EntityGraphType.SIMPLE, positionDto.getProductId());
            if (product == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.PRODUCT_NOT_EXIST.getMsg());
            }
            Store store = storeDao.findById(EntityGraphType.SIMPLE, positionDto.getStoreId());
            if (store == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.STORE_NOT_EXIST.getMsg());
            }
            position.setStore(store);
            position.setProduct(product);
            Position savedPosition = positionDao.save(position);
            Long cost = PriceMonitoringUtils.getMinorCostFromString(positionDto.getCost());
            CostDynamic dynamic = new CostDynamic(position, cost, positionDto.getCostDate());
            costDynamicDao.save(dynamic);
            return savedPosition;
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Position updatePosition(PositionChangeDto positionDto) throws InternalServiceException, EntityNotFoundException {
        try {
            if (positionDao.findById(EntityGraphType.SIMPLE, positionDto.getId()) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.POSITION_NOT_EXIST.getMsg());
            }
            Position position = dtoMapper.mapping(positionDto, Position.class);
            Product product = productDao.findById(EntityGraphType.SIMPLE, positionDto.getProductId());
            if (product == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.PRODUCT_NOT_EXIST.getMsg());
            }
            Store store = storeDao.findById(EntityGraphType.SIMPLE, positionDto.getStoreId());
            if (store == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.STORE_NOT_EXIST.getMsg());
            }
            position.setStore(store);
            position.setProduct(product);
            return positionDao.update(position);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deletePosition(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            if (positionDao.findById(EntityGraphType.SIMPLE, id) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.POSITION_NOT_EXIST.getMsg());
            }
            positionDao.delete(id);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PositionExtendedDto findPositionById(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            Position position = positionDao.findById(EntityGraphType.EXTENDED, id);
            if (position == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.POSITION_NOT_EXIST.getMsg());
            }
            return dtoMapper.mapping(position, PositionExtendedDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionSimpleDto> findPositionsWithCurrentCosts(Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            return dtoMapper.toListMapping(positionDao.findAll(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey)), PositionSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionSimpleDto> findPositionsWithCurrentCostsByStoreId(Long storeId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Position_.store, Store_.id, storeId);
            return dtoMapper.toListMapping(positionDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings), PositionSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionSimpleDto> findPositionsWithCurrentCostsByProductId(Long productId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Position_.product, Product_.id, productId);
            return dtoMapper.toListMapping(positionDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings), PositionSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionSimpleDto> findPositionsWithCurrentCostsByProductIdAndStoreId(Long productId, Long storeId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Position_.product, Product_.id, productId)
                    .putJoinToSettingsWithEqualInstruction(Position_.store, Store_.id, storeId);
            return dtoMapper.toListMapping(positionDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings), PositionSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForPositions() throws InternalServiceException {
        try {
            return positionDao.getCount();
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForPositionsByStoreId(Long storeId) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Position_.store, Store_.id, storeId);
            return positionDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    public Long getCountForPositionsByStoreIdAndProductId(Long storeId, Long productId) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Position_.store, Store_.id, storeId)
                    .putJoinToSettingsWithEqualInstruction(Position_.product, Product_.id, productId);
            return positionDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForPositionsByProductId(Long productId) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Position_.product, Product_.id, productId);
            return positionDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }
}
