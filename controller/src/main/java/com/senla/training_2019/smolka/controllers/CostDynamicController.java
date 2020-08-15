package com.senla.training_2019.smolka.controllers;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.ICostDynamicService;
import com.senla.training_2019.smolka.model.dto.CollectionDto;
import com.senla.training_2019.smolka.model.dto.MessageDto;
import com.senla.training_2019.smolka.model.dto.update_dto.CostDynamicChangeDto;
import com.senla.training_2019.smolka.model.dto.info_dto.StoreCostDifferenceDto;
import com.senla.training_2019.smolka.model.dto.CostDynamicDto;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/costs")
public class CostDynamicController {

    private final static String CREATE_IS_SUCCESS = "Cost created successfully!";
    private final static String UPDATE_IS_SUCCESS = "Cost updated successfully!";
    private final static String DELETE_IS_SUCCESS = "Cost removed successfully!";
    private final static String CREATE_FROM_FILE_IS_SUCCESS = "Products from file created successfully!";
    private final ICostDynamicService costDynamicService;

    public CostDynamicController(ICostDynamicService costDynamicService) {
        this.costDynamicService = costDynamicService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> createCost(@RequestBody @Validated(CreateMode.class) CostDynamicChangeDto costDynamicDto) throws EntityNotFoundException, InternalServiceException {
        System.out.println(costDynamicDto.getCostDate());
        costDynamicService.createCost(costDynamicDto);
        return ResponseEntity.ok(new MessageDto(CREATE_IS_SUCCESS));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> updateCost(@RequestBody @Validated(UpdateMode.class) CostDynamicChangeDto costDynamicDto) throws EntityNotFoundException, InternalServiceException {
        costDynamicService.updateCost(costDynamicDto);
        return ResponseEntity.ok(new MessageDto(UPDATE_IS_SUCCESS));
    }

    @PostMapping("/csv")
    public ResponseEntity<MessageDto> createFromFile(@RequestParam("file") MultipartFile file) throws IOException, EntityNotFoundException, InternalServiceException {
        costDynamicService.createCostsFromCsvText(new String(file.getBytes()));
        return ResponseEntity.ok(new MessageDto(CREATE_FROM_FILE_IS_SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> deleteCost(@RequestParam(value = "id") Long id) throws InternalServiceException, EntityNotFoundException {
        costDynamicService.deleteCost(id);
        return ResponseEntity.ok(new MessageDto(DELETE_IS_SUCCESS));
    }

    @GetMapping
    public ResponseEntity<CollectionDto> getCostDynamicForPosition(@RequestParam(value = "positionId") Long positionId,
                                                                   @RequestParam(value = "page", required = false) Integer page,
                                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                   @RequestParam(value = "sortKey", required = false) String sortKey) throws EntityNotFoundException, InternalServiceException {
        List<CostDynamicDto> costDynamicDtoList = costDynamicService.findCostDynamicsByPositionId(positionId, page, pageSize, sortKey);
        if (costDynamicDtoList == null) {
            return null;
        }
        CollectionDto collectionDto = new CollectionDto((long)costDynamicDtoList.size(), costDynamicDtoList);
        return ResponseEntity.ok(collectionDto);
    }

    @GetMapping("/difference")
    public ResponseEntity<StoreCostDifferenceDto> getDifferenceBetweenStores(@RequestParam(value = "firstId") Long firstStoreId,
                                                                             @RequestParam(value = "secondId") Long secondStoreId) throws EntityNotFoundException, InternalServiceException {
        return ResponseEntity.ok(costDynamicService.getDifferenceBetweenCostAtStores(firstStoreId, secondStoreId));
    }

    @GetMapping("/dynamic")
    public ResponseEntity<Map<String, String>> getDynamicForProductByDates(@RequestParam("date1") @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss") Date date1,
                                                                         @RequestParam("date2") @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss") Date date2,
                                                                         @RequestParam("productId") Long productId,
                                                                         @RequestParam("storeId") Long storeId) throws EntityNotFoundException, InternalServiceException {
        return ResponseEntity.ok(costDynamicService.getDynamicForProductAndStoreBetweenDates(productId, storeId, date1, date2));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostDynamicDto> getCostById(@PathVariable Long id) throws InternalServiceException, EntityNotFoundException {
        return ResponseEntity.ok(costDynamicService.findCostById(id));
    }
}
