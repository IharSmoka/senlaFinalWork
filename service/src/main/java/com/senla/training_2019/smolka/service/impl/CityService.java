package com.senla.training_2019.smolka.service.impl;

import com.senla.training_2019.smolka.api.dao.ICityDao;
import com.senla.training_2019.smolka.api.dao.ICountryDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.ICityService;
import com.senla.training_2019.smolka.mappers.DtoMapper;
import com.senla.training_2019.smolka.model.dto.update_dto.CityChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.CityExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.CitySimpleDto;
import com.senla.training_2019.smolka.model.entities.*;
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
public class CityService implements ICityService {

    private static final Logger logger = LogManager.getLogger(CityService.class);
    private final ICityDao cityDao;
    private final ICountryDao countryDao;
    private final DtoMapper dtoMapper;

    @Autowired
    public CityService(ICityDao cityDao, ICountryDao countryDao, DtoMapper dtoMapper) {
        this.cityDao = cityDao;
        this.countryDao = countryDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public City createCity(CityChangeDto cityDto) throws InternalServiceException, EntityNotFoundException {
        try {
            City city = dtoMapper.mapping(cityDto, City.class);
            Country country = countryDao.findById(EntityGraphType.SIMPLE, cityDto.getCountryId());
            if (country == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.CITY_NOT_EXIST.getMsg());
            }
            city.setCountry(country);
            return cityDao.save(city);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public City updateCity(CityChangeDto cityDto) throws InternalServiceException, EntityNotFoundException {
        try {
            if (cityDao.findById(EntityGraphType.SIMPLE, cityDto.getId()) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.CITY_NOT_EXIST.getMsg());
            }
            City city = dtoMapper.mapping(cityDto, City.class);
            Country country = countryDao.findById(EntityGraphType.SIMPLE, cityDto.getCountryId());
            if (country == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.COUNTRY_NOT_EXIST.getMsg());
            }
            city.setCountry(country);
            return cityDao.update(city);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteCity(Integer id) throws InternalServiceException, EntityNotFoundException {
        try {
            if (cityDao.findById(EntityGraphType.SIMPLE, id) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.CITY_NOT_EXIST.getMsg());
            }
            cityDao.delete(id);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CityExtendedDto findCityById(Integer id) throws InternalServiceException, EntityNotFoundException {
        try {
            City city = cityDao.findById(EntityGraphType.EXTENDED, id);
            if (city == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.CITY_NOT_EXIST.getMsg());
            }
            return dtoMapper.mapping(city, CityExtendedDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitySimpleDto> findCities(Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            List<City> cities = cityDao.findAll(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey));
            return dtoMapper.toListMapping(cities, CitySimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitySimpleDto> findCitiesByName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(City_.cityName, name);
            List<City> cities = cityDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(cities, CitySimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitySimpleDto> findCitiesByCountryId(Integer countryId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(City_.country, Country_.id, countryId);
            List<City> cities = cityDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(cities, CitySimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitySimpleDto> findCitiesByNameAndCountryId(String name, Integer countryId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(City_.country, Country_.id, countryId).putToSettingsWithLikeInstruction(City_.cityName, name);
            List<City> cities = cityDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(cities, CitySimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForCities() throws InternalServiceException {
        try {
            return cityDao.getCount();
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForCitiesByName(String name) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(City_.cityName, name);
            return cityDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForCitiesByCountryId(Integer countryId) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(City_.country, Country_.id, countryId);
            return cityDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForCitiesByNameAndCountryId(String name, Integer countryId) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(City_.country, Country_.id, countryId).putToSettingsWithLikeInstruction(City_.cityName, name);
            return cityDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }
}
