package com.senla.training_2019.smolka.service.impl;

import com.senla.training_2019.smolka.api.dao.IAddressDao;
import com.senla.training_2019.smolka.api.dao.ICityDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IAddressService;
import com.senla.training_2019.smolka.mappers.DtoMapper;
import com.senla.training_2019.smolka.model.dto.update_dto.AddressChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.AddressExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.AddressSimpleDto;
import com.senla.training_2019.smolka.model.entities.City_;
import com.senla.training_2019.smolka.price_monitoring_utils.sort_utils.PriceMonitoringPaginationSettingsBuilder;
import com.senla.training_2019.smolka.service.impl.exception_messages.EntityNotFoundExceptionMessages;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;
import com.senla.training_2019.smolka.model.entities.Address;
import com.senla.training_2019.smolka.model.entities.Address_;
import com.senla.training_2019.smolka.model.entities.City;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService implements IAddressService {

    private static final Logger logger = LogManager.getLogger(AddressService.class);
    private final IAddressDao addressDao;
    private final ICityDao cityDao;
    private final DtoMapper dtoMapper;

    @Autowired
    public AddressService(IAddressDao addressDao, ICityDao cityDao, DtoMapper dtoMapper) {
        this.addressDao = addressDao;
        this.cityDao = cityDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Address createAddress(AddressChangeDto addressDto) throws InternalServiceException, EntityNotFoundException {
        try {
            Address address = dtoMapper.mapping(addressDto, Address.class);
            City city = cityDao.findById(EntityGraphType.SIMPLE, addressDto.getCityId());
            if (city == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.CITY_NOT_EXIST.getMsg());
            }
            address.setCity(city);
            return addressDao.save(address);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Address updateAddress(AddressChangeDto addressDto) throws InternalServiceException, EntityNotFoundException {
        try {
            if (addressDao.findById(EntityGraphType.SIMPLE, addressDto.getId()) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.ADDRESS_NOT_EXIST.getMsg());
            }
            Address address = dtoMapper.mapping(addressDto, Address.class);
            City city = cityDao.findById(EntityGraphType.SIMPLE, addressDto.getCityId());
            if (city == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.CITY_NOT_EXIST.getMsg());
            }
            address.setCity(city);
            return addressDao.update(address);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteAddress(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            if (addressDao.findById(EntityGraphType.SIMPLE, id) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.ADDRESS_NOT_EXIST.getMsg());
            }
            addressDao.delete(id);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AddressExtendedDto findAddressById(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            Address address = addressDao.findById(EntityGraphType.EXTENDED, id);
            if (address == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.ADDRESS_NOT_EXIST.getMsg());
            }
            return dtoMapper.mapping(address, AddressExtendedDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressSimpleDto> findAddresses(Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            List<Address> addresses = addressDao.findAll(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey));
            return dtoMapper.toListMapping(addresses, AddressSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressSimpleDto> findAddressesByName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(Address_.streetName, name);
            List<Address> addresses = addressDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(addresses, AddressSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    public List<AddressSimpleDto> findAddressesByCityId(Integer cityId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Address_.city, City_.id, cityId);
            List<Address> addresses = addressDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(addresses, AddressSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressSimpleDto> findAddressesByNameAndCityId(String name, Integer cityId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Address_.city, City_.id, cityId)
                    .putToSettingsWithLikeInstruction(Address_.streetName, name);
            List<Address> addresses = addressDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(addresses, AddressSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForAddresses() throws InternalServiceException {
        try {
            return addressDao.getCount();
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForAddressesByName(String name) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(Address_.streetName, name);
            return addressDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForAddressesByCityId(Integer cityId) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Address_.city, City_.id, cityId);
            return addressDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForAddressesByNameAndCityId(String name, Integer cityId) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Address_.city, City_.id, cityId)
                    .putToSettingsWithLikeInstruction(Address_.streetName, name);
            return addressDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }
}
