package com.senla.training_2019.smolka.api.service;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.model.dto.extended.PhoneExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.PhoneSimpleDto;
import com.senla.training_2019.smolka.model.dto.update_dto.PhoneChangeDto;
import com.senla.training_2019.smolka.model.entities.Phone;

import java.util.List;

public interface IPhoneService {
    void createPhone(PhoneChangeDto phoneChangeDto) throws InternalServiceException, EntityNotFoundException;
    void updatePhone(PhoneChangeDto phoneChangeDto) throws InternalServiceException, EntityNotFoundException;
    void deletePhone(Integer id) throws InternalServiceException, EntityNotFoundException;
    List<PhoneSimpleDto> findAllPhones(Integer page, Integer pageSize, String sortKey);
    List<PhoneSimpleDto> findAllPhonesByStoreId(Long storeId, Integer page, Integer pageSize, String sortKey);
    List<PhoneExtendedDto> findAllPhonesByOperatorCode(String operatorCode, Integer page, Integer pageSize, String sortKey);
    List<PhoneExtendedDto> findAllPhonesByNumber(String number, Integer page, Integer pageSize, String sortKey);
    Long getCountOfPhones();
    Long getCountOfPhonesByStoreId(Long storeId);
    Long getCountOfPhonesByNumber(String like);
    Long getCountOfPhonesByOperatorCode(String operator);
}
