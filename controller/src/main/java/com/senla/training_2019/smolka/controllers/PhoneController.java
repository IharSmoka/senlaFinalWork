package com.senla.training_2019.smolka.controllers;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IMakerService;
import com.senla.training_2019.smolka.api.service.IPhoneService;
import com.senla.training_2019.smolka.model.dto.CollectionDto;
import com.senla.training_2019.smolka.model.dto.MessageDto;
import com.senla.training_2019.smolka.model.dto.update_dto.MakerChangeDto;
import com.senla.training_2019.smolka.model.dto.update_dto.PhoneChangeDto;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/phones")
public class PhoneController {

    private final static String CREATE_IS_SUCCESS = "Phone created successfully!";
    private final static String UPDATE_IS_SUCCESS = "Phone updated successfully!";
    private final static String DELETE_IS_SUCCESS = "Phone removed successfully!";
    private final IPhoneService phoneService;

    @Autowired
    public PhoneController(IPhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> createPhone(@RequestBody @Validated(CreateMode.class) PhoneChangeDto phoneChangeDto) throws EntityNotFoundException, InternalServiceException {
        phoneService.createPhone(phoneChangeDto);
        return ResponseEntity.ok(new MessageDto(CREATE_IS_SUCCESS));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> updatePhone(@RequestBody @Validated(UpdateMode.class) PhoneChangeDto phoneChangeDto) throws EntityNotFoundException, InternalServiceException {
        phoneService.updatePhone(phoneChangeDto);
        return ResponseEntity.ok(new MessageDto(UPDATE_IS_SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> deletePhone(@RequestParam(value = "id") Integer id) throws InternalServiceException, EntityNotFoundException {
        phoneService.deletePhone(id);
        return ResponseEntity.ok(new MessageDto(DELETE_IS_SUCCESS));
    }

    @GetMapping("/count")
    public Long getCountForPhones(@RequestParam(value = "operator", required = false) String operator,
                                  @RequestParam(value = "storeId", required = false) Long storeId,
                                  @RequestParam(value = "numb", required = false) String number) throws InternalServiceException {
        if (operator != null) {
            return phoneService.getCountOfPhonesByOperatorCode(operator);
        }
        if (storeId != null) {
            return phoneService.getCountOfPhonesByStoreId(storeId);
        }
        if (number != null) {
            return phoneService.getCountOfPhonesByNumber(number);
        }
        return phoneService.getCountOfPhones();
    }

    @GetMapping
    public ResponseEntity<CollectionDto> getAllPhones(@RequestParam(value = "operator", required = false) String operator,
                                                      @RequestParam(value = "storeId", required = false) Long storeId,
                                                      @RequestParam(value = "numb", required = false) String number,
                                                      @RequestParam(value = "page") Integer page,
                                                      @RequestParam(value = "pageSize") Integer pageSize,
                                                      @RequestParam(value = "sortKey") String sortKey) {
        if (operator != null) {
            return ResponseEntity.ok(new CollectionDto(phoneService.getCountOfPhonesByOperatorCode(operator), phoneService.findAllPhonesByOperatorCode(operator, page, pageSize, sortKey)));
        }
        if (storeId != null) {
            return ResponseEntity.ok(new CollectionDto(phoneService.getCountOfPhonesByStoreId(storeId), phoneService.findAllPhonesByStoreId(storeId, page, pageSize, sortKey)));
        }
        if (number != null) {
            return ResponseEntity.ok(new CollectionDto(phoneService.getCountOfPhonesByNumber(number), phoneService.findAllPhonesByNumber(number, page, pageSize, sortKey)));
        }
        return ResponseEntity.ok(new CollectionDto(phoneService.getCountOfPhones(), phoneService.findAllPhones(page, pageSize, sortKey)));
    }

}
