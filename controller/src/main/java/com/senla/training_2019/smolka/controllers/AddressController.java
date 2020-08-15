package com.senla.training_2019.smolka.controllers;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IAddressService;
import com.senla.training_2019.smolka.model.dto.CollectionDto;
import com.senla.training_2019.smolka.model.dto.MessageDto;
import com.senla.training_2019.smolka.model.dto.update_dto.AddressChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.AddressExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.AddressSimpleDto;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final static String CREATE_IS_SUCCESS = "Address created successfully!";
    private final static String UPDATE_IS_SUCCESS = "Address updated successfully!";
    private final static String DELETE_IS_SUCCESS = "Address removed successfully!";
    private final IAddressService addressService;

    public AddressController(IAddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> createAddress(@RequestBody @Validated(CreateMode.class) AddressChangeDto addressDto) throws EntityNotFoundException, InternalServiceException {
        addressService.createAddress(addressDto);
        return ResponseEntity.ok(new MessageDto(CREATE_IS_SUCCESS));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> updateAddress(@RequestBody @Validated(UpdateMode.class) AddressChangeDto addressDto) throws EntityNotFoundException, InternalServiceException {
        addressService.updateAddress(addressDto);
        return ResponseEntity.ok(new MessageDto(UPDATE_IS_SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> deleteAddress(@RequestParam(value = "id") Long id) throws InternalServiceException, EntityNotFoundException {
        addressService.deleteAddress(id);
        return ResponseEntity.ok(new MessageDto(DELETE_IS_SUCCESS));
    }

    @GetMapping("/count")
    public Long getCount(@RequestParam(value = "cityId", required = false) Integer id,
                         @RequestParam(value = "name", required = false) String name) throws InternalServiceException {
        if (id == null && name == null) {
            return addressService.getCountForAddresses();
        }
        if (id != null && name != null) {
            return addressService.getCountForAddressesByNameAndCityId(name, id);
        }
        if (id != null) {
            return addressService.getCountForAddressesByCityId(id);
        }
        return addressService.getCountForAddressesByName(name);
    }

    @GetMapping
    public ResponseEntity<CollectionDto> getAllAddresses(@RequestParam(value = "page", required = false) Integer page,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                         @RequestParam(value = "cityId", required = false) Integer id,
                                                         @RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "sortKey", required = false) String sortKey) throws InternalServiceException {
        List<AddressSimpleDto> addressDtoList = null;
        Long count = 0L;
        if (id == null && name == null) {
            addressDtoList = addressService.findAddresses(page, pageSize, sortKey);
            count = addressService.getCountForAddresses();
            return ResponseEntity.ok(new CollectionDto(count, addressDtoList));
        }
        if (id != null && name != null) {
            addressDtoList = addressService.findAddressesByNameAndCityId(name, id, page, pageSize, sortKey);
            count = addressService.getCountForAddressesByNameAndCityId(name, id);
            return ResponseEntity.ok(new CollectionDto(count, addressDtoList));
        }
        if (id == null) {
            addressDtoList = addressService.findAddressesByName(name, page, pageSize, sortKey);
            count = addressService.getCountForAddressesByCityId(id);
            return ResponseEntity.ok(new CollectionDto(count, addressDtoList));
        }
        addressDtoList = addressService.findAddressesByCityId(id, page, pageSize, sortKey);
        count = addressService.getCountForAddressesByCityId(id);
        return ResponseEntity.ok(new CollectionDto(count, addressDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressExtendedDto> getAddressById(@PathVariable Long id) throws EntityNotFoundException, InternalServiceException {
        return ResponseEntity.ok(addressService.findAddressById(id));
    }
}
