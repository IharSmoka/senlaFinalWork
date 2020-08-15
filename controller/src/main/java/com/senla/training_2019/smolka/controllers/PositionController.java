package com.senla.training_2019.smolka.controllers;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IPositionService;
import com.senla.training_2019.smolka.model.dto.CollectionDto;
import com.senla.training_2019.smolka.model.dto.MessageDto;
import com.senla.training_2019.smolka.model.dto.extended.PositionExtendedDto;
import com.senla.training_2019.smolka.model.dto.update_dto.PositionChangeDto;
import com.senla.training_2019.smolka.model.dto.simple.PositionSimpleDto;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final static String CREATE_IS_SUCCESS = "Position created successfully!";
    private final static String UPDATE_IS_SUCCESS = "Position updated successfully!";
    private final static String DELETE_IS_SUCCESS = "Position removed successfully!";
    private final IPositionService positionService;

    public PositionController(IPositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> createPosition(@RequestBody @Validated(CreateMode.class) PositionChangeDto positionDto) throws EntityNotFoundException, InternalServiceException {
        positionService.createPosition(positionDto);
        return ResponseEntity.ok(new MessageDto(CREATE_IS_SUCCESS));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> updatePosition(@RequestBody @Validated(UpdateMode.class) PositionChangeDto positionDto) throws EntityNotFoundException, InternalServiceException {
        positionService.updatePosition(positionDto);
        return ResponseEntity.ok(new MessageDto(UPDATE_IS_SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> deletePosition(@RequestParam(value = "id") Long id) throws InternalServiceException, EntityNotFoundException {
        positionService.deletePosition(id);
        return ResponseEntity.ok(new MessageDto(DELETE_IS_SUCCESS));
    }

    @GetMapping("/count")
    public Long getCountForPositions(@RequestParam(value = "productId", required = false) Long productId,
                                     @RequestParam(value = "storeId", required = false) Long storeId) throws InternalServiceException {
        if (productId == null && storeId == null) {
            return positionService.getCountForPositions();
        }
        if (productId != null && storeId != null) {
            return positionService.getCountForPositionsByStoreIdAndProductId(storeId, productId);
        }
        if (productId != null) {
            return positionService.getCountForPositionsByProductId(productId);
        }
        return positionService.getCountForPositionsByStoreId(storeId);
    }

    @GetMapping
    public ResponseEntity<CollectionDto> getAllPositions(@RequestParam(value = "page", required = false) Integer page,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                         @RequestParam(value = "productId", required = false) Long productId,
                                                         @RequestParam(value = "storeId", required = false) Long storeId,
                                                         @RequestParam(value = "sortKey", required = false) String sortKey) throws InternalServiceException {
        List<PositionSimpleDto> positionDtoList = null;
        Long count = 0L;
        if (productId == null && storeId == null) {
            positionDtoList = positionService.findPositionsWithCurrentCosts(page, pageSize, sortKey);
            count = positionService.getCountForPositions();
            return ResponseEntity.ok(new CollectionDto(count, positionDtoList));
        }
        if (productId != null && storeId != null) {
            positionDtoList = positionService.findPositionsWithCurrentCostsByProductIdAndStoreId(productId, storeId, page, pageSize, sortKey);
            count = positionService.getCountForPositionsByStoreIdAndProductId(storeId, productId);
            return ResponseEntity.ok(new CollectionDto(count, positionDtoList));
        }
        if (productId != null) {
            positionDtoList = positionService.findPositionsWithCurrentCostsByProductId(productId, page, pageSize, sortKey);
            count = positionService.getCountForPositionsByProductId(productId);
            return ResponseEntity.ok(new CollectionDto(count, positionDtoList));
        }
        positionDtoList = positionService.findPositionsWithCurrentCostsByStoreId(storeId, page, pageSize, sortKey);
        count = positionService.getCountForPositionsByStoreId(storeId);
        return ResponseEntity.ok(new CollectionDto(count, positionDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionExtendedDto> getPositionById(@PathVariable Long id) throws InternalServiceException, EntityNotFoundException {
        return ResponseEntity.ok(positionService.findPositionById(id));
    }
}
