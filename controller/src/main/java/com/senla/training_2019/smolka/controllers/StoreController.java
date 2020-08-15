package com.senla.training_2019.smolka.controllers;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IStoreService;
import com.senla.training_2019.smolka.model.dto.CollectionDto;
import com.senla.training_2019.smolka.model.dto.MessageDto;
import com.senla.training_2019.smolka.model.dto.update_dto.StoreChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.StoreExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.StoreSimpleDto;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {

    private final static String CREATE_IS_SUCCESS = "Store created successfully!";
    private final static String UPDATE_IS_SUCCESS = "Store updated successfully!";
    private final static String DELETE_IS_SUCCESS = "Store removed successfully!";
    private final IStoreService storeService;

    public StoreController(IStoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> createStore(@RequestBody @Validated(CreateMode.class) StoreChangeDto storeDto) throws EntityNotFoundException, InternalServiceException {
        storeService.createStore(storeDto);
        return ResponseEntity.ok(new MessageDto(CREATE_IS_SUCCESS));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> updateStore(@RequestBody @Validated(UpdateMode.class) StoreChangeDto storeDto) throws EntityNotFoundException, InternalServiceException {
        storeService.updateStore(storeDto);
        return ResponseEntity.ok(new MessageDto(UPDATE_IS_SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> deleteStore(@RequestParam(value = "id") Long id) throws InternalServiceException, EntityNotFoundException {
        storeService.deleteStore(id);
        return ResponseEntity.ok(new MessageDto(DELETE_IS_SUCCESS));
    }

    @GetMapping("/count")
    public Long getCountForStores(@RequestParam(value = "name", required = false) String name) throws InternalServiceException {
        return name == null ? storeService.getCountForStores() : storeService.getCountForStoresByName(name);
    }

    @GetMapping
    public ResponseEntity<CollectionDto> getAllStores(@RequestParam(value = "page", required = false) Integer page,
                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                          @RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "sortKey", required = false) String sortKey) throws InternalServiceException {
        List<StoreSimpleDto> categoryDtoList = null;
        Long count = 0L;
        if (name == null) {
            categoryDtoList = storeService.findStores(page, pageSize, sortKey);
            count = storeService.getCountForStores();
        } else {
            categoryDtoList = storeService.findStoresByName(name, page, pageSize, sortKey);
            count = storeService.getCountForStoresByName(name);
        }
        return ResponseEntity.ok(new CollectionDto(count, categoryDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreExtendedDto> getStoreById(@PathVariable Long id) throws InternalServiceException, EntityNotFoundException {
        return ResponseEntity.ok(storeService.findStoreById(id));
    }
}
