package com.senla.training_2019.smolka.api.service;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.model.dto.update_dto.MakerChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.MakerExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.MakerSimpleDto;
import com.senla.training_2019.smolka.model.entities.Maker;

import java.util.List;

public interface IMakerService {
    Maker createMaker(MakerChangeDto maker) throws InternalServiceException, EntityNotFoundException;
    Maker updateMaker(MakerChangeDto maker) throws InternalServiceException, EntityNotFoundException;
    void deleteMaker(Long id) throws InternalServiceException, EntityNotFoundException;
    List<MakerSimpleDto> findMakers(Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    MakerExtendedDto findMakerById(Long id) throws InternalServiceException;
    List<MakerSimpleDto> findMakersByName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<MakerSimpleDto> findMakersByCountryId(Integer countryId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<MakerSimpleDto> findMakersByNameAndCountryId(String name, Integer countryId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    Long getCountForMakers() throws InternalServiceException;
    Long getCountForMakersByName(String name) throws InternalServiceException;
    Long getCountForMakersByCountryId(Integer countryId) throws InternalServiceException;
    Long getCountForMakersByNameAndCountryId(String name, Integer countryId) throws InternalServiceException;
}
