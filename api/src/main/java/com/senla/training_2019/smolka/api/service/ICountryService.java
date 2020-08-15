package com.senla.training_2019.smolka.api.service;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.model.dto.CountryDto;
import com.senla.training_2019.smolka.model.entities.Country;

import java.util.List;

public interface ICountryService {
    Country createCountry(CountryDto country) throws InternalServiceException;
    Country updateCountry(CountryDto country) throws InternalServiceException, EntityNotFoundException;
    void deleteCountry(Integer id) throws InternalServiceException, EntityNotFoundException;
    CountryDto findCountryById(Integer id) throws InternalServiceException, EntityNotFoundException;
    List<CountryDto> findCountries(Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<CountryDto> findCountriesByName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    Long getCountForCountries() throws InternalServiceException;
    Long getCountForCountriesByName(String name) throws InternalServiceException;
}
