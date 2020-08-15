package com.senla.training_2019.smolka.service.impl;

import com.senla.training_2019.smolka.api.dao.IAddressDao;
import com.senla.training_2019.smolka.api.dao.IStoreDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IStoreService;
import com.senla.training_2019.smolka.mappers.DtoMapper;
import com.senla.training_2019.smolka.model.dto.update_dto.StoreChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.StoreExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.StoreSimpleDto;
import com.senla.training_2019.smolka.price_monitoring_utils.sort_utils.PriceMonitoringPaginationSettingsBuilder;
import com.senla.training_2019.smolka.service.impl.exception_messages.EntityNotFoundExceptionMessages;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;
import com.senla.training_2019.smolka.model.entities.Address;
import com.senla.training_2019.smolka.model.entities.Store;
import com.senla.training_2019.smolka.model.entities.Store_;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreService implements IStoreService {

    private static final Logger logger = LogManager.getLogger(StoreService.class);
    private final IStoreDao storeDao;
    private final IAddressDao addressDao;
    private final DtoMapper dtoMapper;

    @Autowired
    public StoreService(IStoreDao storeDao, IAddressDao addressDao, DtoMapper dtoMapper) {
        this.storeDao = storeDao;
        this.addressDao = addressDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Store createStore(StoreChangeDto storeDto) throws InternalServiceException, EntityNotFoundException {
        try {
            Store store = dtoMapper.mapping(storeDto, Store.class);
            Address address = addressDao.findById(EntityGraphType.SIMPLE, storeDto.getAddressId());
            if (address == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.STORE_NOT_EXIST.getMsg());
            }
            store.setAddress(address);
            return storeDao.save(store);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Store updateStore(StoreChangeDto storeDto) throws InternalServiceException, EntityNotFoundException {
        try {
            if (storeDao.findById(EntityGraphType.SIMPLE, storeDto.getId()) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.STORE_NOT_EXIST.getMsg());
            }
            Store store = dtoMapper.mapping(storeDto, Store.class);
            Address address = addressDao.findById(EntityGraphType.SIMPLE, storeDto.getAddressId());
            if (address == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.ADDRESS_NOT_EXIST.getMsg());
            }
            store.setAddress(address);
            return storeDao.update(store);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteStore(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            if (storeDao.findById(EntityGraphType.SIMPLE, id) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.STORE_NOT_EXIST.getMsg());
            }
            storeDao.delete(id);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    public StoreExtendedDto findStoreById(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            Store store = storeDao.findById(EntityGraphType.EXTENDED, id);
            if (store == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.STORE_NOT_EXIST.getMsg());
            }
            return dtoMapper.mapping(store, StoreExtendedDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreSimpleDto> findStores(Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            List<Store> stores = storeDao.findAll(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey));
            return dtoMapper.toListMapping(stores, StoreSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreSimpleDto> findStoresByName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(Store_.storeName, name);
            List<Store> stores = storeDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(stores, StoreSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Store> findStoresByIdArray(Long... storeIdArr) throws InternalServiceException {
        try {
            return storeDao.findStoresByIdArray(EntityGraphType.EXTENDED, storeIdArr);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForStores() throws InternalServiceException {
        try {
            return storeDao.getCount();
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForStoresByName(String name) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(Store_.storeName, name);
            return storeDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }
}
