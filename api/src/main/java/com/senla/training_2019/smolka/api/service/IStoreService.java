package com.senla.training_2019.smolka.api.service;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.model.dto.update_dto.StoreChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.StoreExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.StoreSimpleDto;
import com.senla.training_2019.smolka.model.entities.Store;

import java.util.List;

public interface IStoreService {
    Store createStore(StoreChangeDto store) throws InternalServiceException, EntityNotFoundException;
    Store updateStore(StoreChangeDto store) throws InternalServiceException, EntityNotFoundException;
    void deleteStore(Long id) throws InternalServiceException, EntityNotFoundException;
    StoreExtendedDto findStoreById(Long id) throws InternalServiceException, EntityNotFoundException;
    List<StoreSimpleDto> findStores(Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<StoreSimpleDto> findStoresByName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<Store> findStoresByIdArray(Long... storeIdArr) throws InternalServiceException;
    Long getCountForStores() throws InternalServiceException;
    Long getCountForStoresByName(String name) throws InternalServiceException;
}
