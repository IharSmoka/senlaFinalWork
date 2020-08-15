package com.senla.training_2019.smolka.service.impl;

import com.senla.training_2019.smolka.api.dao.ICountryDao;
import com.senla.training_2019.smolka.api.dao.IMakerDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IMakerService;
import com.senla.training_2019.smolka.mappers.DtoMapper;
import com.senla.training_2019.smolka.model.dto.update_dto.MakerChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.MakerExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.MakerSimpleDto;
import com.senla.training_2019.smolka.model.entities.Country_;
import com.senla.training_2019.smolka.price_monitoring_utils.sort_utils.PriceMonitoringPaginationSettingsBuilder;
import com.senla.training_2019.smolka.service.impl.exception_messages.EntityNotFoundExceptionMessages;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;
import com.senla.training_2019.smolka.model.entities.Country;
import com.senla.training_2019.smolka.model.entities.Maker;
import com.senla.training_2019.smolka.model.entities.Maker_;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MakerService implements IMakerService {

    private static final Logger logger = LogManager.getLogger(MakerService.class);
    private final IMakerDao makerDao;
    private final ICountryDao countryDao;
    private final DtoMapper dtoMapper;

    @Autowired
    public MakerService(IMakerDao makerDao, ICountryDao countryDao, DtoMapper dtoMapper) {
        this.makerDao = makerDao;
        this.countryDao = countryDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Maker createMaker(MakerChangeDto makerDto) throws InternalServiceException, EntityNotFoundException {
        try {
            Maker maker = dtoMapper.mapping(makerDto, Maker.class);
            Country country = countryDao.findById(EntityGraphType.SIMPLE, makerDto.getCountryId());
            if (country == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.COUNTRY_NOT_EXIST.getMsg());
            }
            maker.setCountry(country);
            return makerDao.save(maker);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Maker updateMaker(MakerChangeDto makerDto) throws InternalServiceException, EntityNotFoundException {
        try {
            if (makerDao.findById(EntityGraphType.SIMPLE, makerDto.getId()) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.MAKER_NOT_EXIST.getMsg());
            }
            Maker maker = dtoMapper.mapping(makerDto, Maker.class);
            Country country = countryDao.findById(EntityGraphType.SIMPLE, makerDto.getCountryId());
            if (country == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.COUNTRY_NOT_EXIST.getMsg());
            }
            maker.setCountry(country);
            return makerDao.update(maker);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteMaker(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            if (makerDao.findById(EntityGraphType.SIMPLE, id) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.MAKER_NOT_EXIST.getMsg());
            }
            makerDao.delete(id);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MakerSimpleDto> findMakers(Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            List<Maker> makers = makerDao.findAll(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey));
            return dtoMapper.toListMapping(makers, MakerSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MakerExtendedDto findMakerById(Long id) throws InternalServiceException {
        try {
            Maker maker = makerDao.findById(EntityGraphType.EXTENDED, id);
            return dtoMapper.mapping(maker, MakerExtendedDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MakerSimpleDto> findMakersByName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(Maker_.makerName, name);
            List<Maker> makers = makerDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(makers, MakerSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MakerSimpleDto> findMakersByCountryId(Integer countryId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Maker_.country, Country_.id, countryId);
            List<Maker> makers = makerDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(makers, MakerSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MakerSimpleDto> findMakersByNameAndCountryId(String name, Integer countryId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Maker_.country, Country_.id, countryId)
                    .putToSettingsWithLikeInstruction(Maker_.makerName, name);
            List<Maker> makers = makerDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(makers, MakerSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForMakers() throws InternalServiceException {
        try {
            return makerDao.getCount();
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForMakersByName(String name) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(Maker_.makerName, name);
            return makerDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForMakersByCountryId(Integer countryId) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Maker_.country, Country_.id, countryId);
            return makerDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForMakersByNameAndCountryId(String name, Integer countryId) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Maker_.country, Country_.id, countryId)
                    .putToSettingsWithLikeInstruction(Maker_.makerName, name);
            return makerDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }
}
