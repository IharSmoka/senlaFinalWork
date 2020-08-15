package com.senla.training_2019.smolka.api.service;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.model.dto.update_dto.CityChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.CityExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.CitySimpleDto;
import com.senla.training_2019.smolka.model.entities.City;

import java.util.List;

public interface ICityService {
    City createCity(CityChangeDto city) throws InternalServiceException, EntityNotFoundException;
    City updateCity(CityChangeDto city) throws InternalServiceException, EntityNotFoundException;
    void deleteCity(Integer id) throws InternalServiceException, EntityNotFoundException;
    CityExtendedDto findCityById(Integer id) throws InternalServiceException, EntityNotFoundException;
    List<CitySimpleDto> findCities(Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<CitySimpleDto> findCitiesByName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<CitySimpleDto> findCitiesByCountryId(Integer countryId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<CitySimpleDto> findCitiesByNameAndCountryId(String name, Integer countryId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    Long getCountForCities() throws InternalServiceException;
    Long getCountForCitiesByName(String name) throws InternalServiceException;
    Long getCountForCitiesByCountryId(Integer countryId) throws InternalServiceException;
    Long getCountForCitiesByNameAndCountryId(String name, Integer countryId) throws InternalServiceException;
}
