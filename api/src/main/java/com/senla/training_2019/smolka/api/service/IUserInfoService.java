package com.senla.training_2019.smolka.api.service;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.model.dto.update_dto.UserChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.UserInfoExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.UserInfoSimpleDto;
import com.senla.training_2019.smolka.model.entities.UserInfo;

import java.util.List;

public interface IUserInfoService {
    UserInfo createUser(UserChangeDto user) throws InternalServiceException;
    UserInfo updateUser(UserChangeDto user, String changerUsername) throws InternalServiceException, EntityNotFoundException;
    void deleteUser(Long id) throws InternalServiceException, EntityNotFoundException;
    void setModeratorRoleToUser(Long id) throws InternalServiceException, EntityNotFoundException;
    UserInfoExtendedDto findUserById(Long id) throws InternalServiceException, EntityNotFoundException;
    List<UserInfoSimpleDto> findUsersByFirstName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<UserInfoSimpleDto> findUsersBySecondName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    Long getCountForUsersByFirstName(String name) throws InternalServiceException;
    Long getCountForUsersBySecondName(String name) throws InternalServiceException;
}
