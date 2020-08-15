package com.senla.training_2019.smolka.controllers;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.ICityService;
import com.senla.training_2019.smolka.model.dto.CollectionDto;
import com.senla.training_2019.smolka.model.dto.MessageDto;
import com.senla.training_2019.smolka.model.dto.update_dto.CityChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.CityExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.CitySimpleDto;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final static String CREATE_IS_SUCCESS = "City created successfully!";
    private final static String UPDATE_IS_SUCCESS = "City updated successfully!";
    private final static String DELETE_IS_SUCCESS = "City removed successfully!";
    private final ICityService cityService;

    public CityController(ICityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> createCity(@RequestBody @Validated(CreateMode.class) CityChangeDto cityDto) throws EntityNotFoundException, InternalServiceException {
        cityService.createCity(cityDto);
        return ResponseEntity.ok(new MessageDto(CREATE_IS_SUCCESS));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> updateCity(@RequestBody @Validated(UpdateMode.class) CityChangeDto cityDto) throws EntityNotFoundException, InternalServiceException {
        cityService.updateCity(cityDto);
        return ResponseEntity.ok(new MessageDto(UPDATE_IS_SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> deleteCity(@RequestParam(value = "id") Integer id) throws InternalServiceException, EntityNotFoundException {
        cityService.deleteCity(id);
        return ResponseEntity.ok(new MessageDto(DELETE_IS_SUCCESS));
    }

    @GetMapping("/count")
    public Long getCountForCities(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "countryId", required = false) Integer countryId) throws InternalServiceException {
        if (name == null && countryId == null) {
            return cityService.getCountForCities();
        }
        if (name != null && countryId != null) {
            return cityService.getCountForCitiesByNameAndCountryId(name, countryId);
        }
        if (countryId != null) {
            return cityService.getCountForCitiesByCountryId(countryId);
        }
        return cityService.getCountForCitiesByName(name);
    }

    @GetMapping
    public ResponseEntity<CollectionDto> getAllCities(@RequestParam(value = "page", required = false) Integer page,
                                                            @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                            @RequestParam(value = "name", required = false) String name,
                                                            @RequestParam(value = "countryId", required = false) Integer countryId,
                                                            @RequestParam(value = "sortKey", required = false) String sortKey) throws InternalServiceException {
        List<CitySimpleDto> cityDtoList = null;
        Long count = 0L;
        if (name == null && countryId == null) {
            cityDtoList = cityService.findCities(page, pageSize, sortKey);
            count = cityService.getCountForCities();
            return ResponseEntity.ok(new CollectionDto(count, cityDtoList));
        }
        if (name != null && countryId != null) {
            cityDtoList = cityService.findCitiesByNameAndCountryId(name, countryId, page, pageSize, sortKey);
            count = cityService.getCountForCitiesByNameAndCountryId(name, countryId);
            return ResponseEntity.ok(new CollectionDto(count, cityDtoList));
        }
        if (countryId != null) {
            cityDtoList = cityService.findCitiesByCountryId(countryId, page, pageSize, sortKey);
            count = cityService.getCountForCitiesByCountryId(countryId);
            return ResponseEntity.ok(new CollectionDto(count, cityDtoList));
        }
        cityDtoList = cityService.findCitiesByName(name, page, pageSize, sortKey);
        count = cityService.getCountForCitiesByName(name);
        return ResponseEntity.ok(new CollectionDto(count, cityDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityExtendedDto> getCityById(@PathVariable Integer id) throws InternalServiceException, EntityNotFoundException {
        return ResponseEntity.ok(cityService.findCityById(id));
    }
}
