package com.senla.training_2019.smolka.api.service;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.model.dto.update_dto.AddressChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.AddressExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.AddressSimpleDto;
import com.senla.training_2019.smolka.model.entities.Address;

import java.util.List;

public interface IAddressService {
    Address createAddress(AddressChangeDto address) throws InternalServiceException, EntityNotFoundException;
    Address updateAddress(AddressChangeDto address) throws InternalServiceException, EntityNotFoundException;
    void deleteAddress(Long id) throws InternalServiceException, EntityNotFoundException;
    AddressExtendedDto findAddressById(Long id) throws InternalServiceException, EntityNotFoundException;
    List<AddressSimpleDto> findAddresses(Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<AddressSimpleDto> findAddressesByName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<AddressSimpleDto> findAddressesByCityId(Integer cityId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<AddressSimpleDto> findAddressesByNameAndCityId(String name, Integer cityId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    Long getCountForAddresses() throws InternalServiceException;
    Long getCountForAddressesByName(String name) throws InternalServiceException;
    Long getCountForAddressesByCityId(Integer cityId) throws InternalServiceException;
    Long getCountForAddressesByNameAndCityId(String name, Integer cityId) throws InternalServiceException;
}
