package com.senla.training_2019.smolka.service.impl;

import com.senla.training_2019.smolka.api.dao.ICredentialDao;
import com.senla.training_2019.smolka.api.dao.IUserInfoDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IUserInfoService;
import com.senla.training_2019.smolka.mappers.DtoMapper;
import com.senla.training_2019.smolka.model.dto.simple.UserInfoSimpleDto;
import com.senla.training_2019.smolka.model.dto.update_dto.UserChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.UserInfoExtendedDto;
import com.senla.training_2019.smolka.model.entities.Credential;
import com.senla.training_2019.smolka.model.enums.UserRole;
import com.senla.training_2019.smolka.price_monitoring_utils.sort_utils.PriceMonitoringPaginationSettingsBuilder;
import com.senla.training_2019.smolka.service.impl.exception_messages.EntityNotFoundExceptionMessages;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;
import com.senla.training_2019.smolka.model.entities.UserInfo;
import com.senla.training_2019.smolka.model.entities.UserInfo_;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Formatter;
import java.util.List;

@Service
public class UserInfoService implements IUserInfoService {

    private static final Logger logger = LogManager.getLogger(UserInfoService.class);
    private static final String CANNOT_UPDATE_USER_FORMATTED_STRING = "Access denied : Cannot update another user with login %s!";
    private final IUserInfoDao userInfoDao;
    private final ICredentialDao credentialDao;
    private final DtoMapper dtoMapper;

    @Autowired
    public UserInfoService(IUserInfoDao userInfoDao, ICredentialDao credentialDao, DtoMapper dtoMapper) {
        this.userInfoDao = userInfoDao;
        this.credentialDao = credentialDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public UserInfo createUser(UserChangeDto userDto) throws InternalServiceException {
        try {
            UserInfo user = dtoMapper.mapping(userDto, UserInfo.class);
            Credential credential = new Credential(userDto.getLogin(), userDto.getPassword(), UserRole.USER);
            user.setCredential(credentialDao.save(credential));
            return userInfoDao.save(user);
        }
        catch (DataAccessException | IllegalArgumentException exc) {
            logger.error(exc.getMessage(), exc);
            throw new InternalServiceException(exc.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public UserInfo updateUser(UserChangeDto userDto, String changerUserName) throws InternalServiceException, EntityNotFoundException {
        try {
            if (userInfoDao.findById(EntityGraphType.SIMPLE, userDto.getId()) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.USER_NOT_EXIST.getMsg());
            }
            Credential credential = credentialDao.findByUserId(userDto.getId());
            if (!credential.getUsername().equals(changerUserName)) {
                Formatter formatter = new Formatter();
                formatter.format(CANNOT_UPDATE_USER_FORMATTED_STRING, credential.getUsername());
                throw new InternalServiceException(formatter.toString());
            }
            credential.setUsername(userDto.getLogin());
            credential.setPassword(userDto.getPassword());
            credential.setRole(UserRole.USER);
            UserInfo user = dtoMapper.mapping(userDto, UserInfo.class);
            user.setCredential(credential);
            return userInfoDao.update(user);
        }
        catch (DataAccessException | IllegalArgumentException exc) {
            logger.error(exc.getMessage(), exc);
            throw new InternalServiceException(exc.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteUser(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            if (userInfoDao.findById(EntityGraphType.SIMPLE, id) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.USER_NOT_EXIST.getMsg());
            }
            userInfoDao.delete(id);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void setModeratorRoleToUser(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            UserInfo userInfo = userInfoDao.findById(EntityGraphType.EXTENDED, id);
            if (userInfo == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.USER_NOT_EXIST.getMsg());
            }
            userInfo.getCredential().setRole(UserRole.MODERATOR);
            userInfoDao.update(userInfo);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoExtendedDto findUserById(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            UserInfo userInfo = userInfoDao.findById(EntityGraphType.EXTENDED, id);
            if (userInfo == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.USER_NOT_EXIST.getMsg());
            }
            return dtoMapper.mapping(userInfo, UserInfoExtendedDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInfoSimpleDto> findUsersByFirstName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(UserInfo_.firstName, name);
            List<UserInfo> userInfoList = userInfoDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(userInfoList, UserInfoSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInfoSimpleDto> findUsersBySecondName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(UserInfo_.secondName, name);
            List<UserInfo> userInfoList = userInfoDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(userInfoList, UserInfoSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForUsersByFirstName(String name) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(UserInfo_.firstName, name);
            return userInfoDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForUsersBySecondName(String name) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(UserInfo_.secondName, name);
            return userInfoDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }
}
