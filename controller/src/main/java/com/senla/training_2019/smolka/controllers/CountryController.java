package com.senla.training_2019.smolka.controllers;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.ICountryService;
import com.senla.training_2019.smolka.model.dto.CollectionDto;
import com.senla.training_2019.smolka.model.dto.CountryDto;
import com.senla.training_2019.smolka.model.dto.MessageDto;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final static String CREATE_IS_SUCCESS = "Country created successfully!";
    private final static String UPDATE_IS_SUCCESS = "Country updated successfully!";
    private final static String DELETE_IS_SUCCESS = "Country removed successfully!";
    private final ICountryService countryService;

    public CountryController(ICountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> createCountry(@RequestBody @Validated(CreateMode.class) CountryDto countryDto) throws InternalServiceException {
        countryService.createCountry(countryDto);
        return ResponseEntity.ok(new MessageDto(CREATE_IS_SUCCESS));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> updateCountry(@RequestBody @Validated(UpdateMode.class) CountryDto countryDto) throws InternalServiceException, EntityNotFoundException {
        countryService.updateCountry(countryDto);
        return ResponseEntity.ok(new MessageDto(UPDATE_IS_SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> deleteCountry(@RequestParam(value = "id") Integer id) throws InternalServiceException, EntityNotFoundException {
        countryService.deleteCountry(id);
        return ResponseEntity.ok(new MessageDto(DELETE_IS_SUCCESS));
    }

    @GetMapping("/count")
    public Long getCountForCountries(@RequestParam(value = "name", required = false) String name) throws InternalServiceException {
        return name == null ? countryService.getCountForCountries() : countryService.getCountForCountriesByName(name);
    }

    @GetMapping
    public ResponseEntity<CollectionDto> getAllCountries(@RequestParam(value = "page", required = false) Integer page,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                         @RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "sortKey", required = false) String sortKey) throws InternalServiceException {
        List<CountryDto> countryDtoList = null;
        Long count = 0L;
        if (name == null) {
            countryDtoList = countryService.findCountries(page, pageSize, sortKey);
            count = countryService.getCountForCountries();
        } else {
            countryDtoList = countryService.findCountriesByName(name, page, pageSize, sortKey);
            count = countryService.getCountForCountriesByName(name);
        }
        return ResponseEntity.ok(new CollectionDto(count, countryDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDto> getCountryById(@PathVariable Integer id) throws InternalServiceException, EntityNotFoundException {
        return ResponseEntity.ok(countryService.findCountryById(id));
    }
}
