package com.senla.training_2019.smolka.controllers;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IMakerService;
import com.senla.training_2019.smolka.model.dto.CollectionDto;
import com.senla.training_2019.smolka.model.dto.MessageDto;
import com.senla.training_2019.smolka.model.dto.update_dto.MakerChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.MakerExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.MakerSimpleDto;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/makers")
public class MakerController {

    private final static String CREATE_IS_SUCCESS = "Maker created successfully!";
    private final static String UPDATE_IS_SUCCESS = "Maker updated successfully!";
    private final static String DELETE_IS_SUCCESS = "Maker removed successfully!";
    private final IMakerService makerService;

    public MakerController(IMakerService makerService) {
        this.makerService = makerService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> createMaker(@RequestBody @Validated(CreateMode.class) MakerChangeDto makerDto) throws EntityNotFoundException, InternalServiceException {
        makerService.createMaker(makerDto);
        return ResponseEntity.ok(new MessageDto(CREATE_IS_SUCCESS));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> updateMaker(@RequestBody @Validated(UpdateMode.class) MakerChangeDto makerDto) throws EntityNotFoundException, InternalServiceException {
        makerService.updateMaker(makerDto);
        return ResponseEntity.ok(new MessageDto(UPDATE_IS_SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> deleteMaker(@RequestParam(value = "id") Long id) throws InternalServiceException, EntityNotFoundException {
        makerService.deleteMaker(id);
        return ResponseEntity.ok(new MessageDto(DELETE_IS_SUCCESS));
    }

    @GetMapping("/count")
    public Long getCountForMakers(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "countryId", required = false) Integer countryId) throws InternalServiceException {
        if (name == null && countryId == null) {
            return makerService.getCountForMakers();
        }
        if (name != null && countryId != null) {
            return makerService.getCountForMakersByNameAndCountryId(name, countryId);
        }
        if (countryId != null) {
            return makerService.getCountForMakersByCountryId(countryId);
        }
        return makerService.getCountForMakersByNameAndCountryId(name, countryId);
    }

    @GetMapping
    public ResponseEntity<CollectionDto> getAllMakers(@RequestParam(value = "page", required = false) Integer page,
                                                      @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                      @RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "countryId", required = false) Integer countryId,
                                                      @RequestParam(value = "sortKey", required = false) String sortKey) throws InternalServiceException {
        List<MakerSimpleDto> makerDtoList = null;
        Long count = 0L;
        if (name == null && countryId == null) {
            makerDtoList = makerService.findMakers(page, pageSize, sortKey);
            count = makerService.getCountForMakers();
            return ResponseEntity.ok(new CollectionDto(count, makerDtoList));
        }
        if (name != null && countryId != null) {
            makerDtoList = makerService.findMakersByNameAndCountryId(name, countryId, page, pageSize, sortKey);
            count = makerService.getCountForMakersByNameAndCountryId(name, countryId);
            return ResponseEntity.ok(new CollectionDto(count, makerDtoList));
        }
        if (countryId != null) {
            makerDtoList = makerService.findMakersByCountryId(countryId, page, pageSize, sortKey);
            count = makerService.getCountForMakersByCountryId(countryId);
            return ResponseEntity.ok(new CollectionDto(count, makerDtoList));
        }
        makerDtoList = makerService.findMakersByName(name, page, pageSize, sortKey);
        count = makerService.getCountForMakersByName(name);
        return ResponseEntity.ok(new CollectionDto(count, makerDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MakerExtendedDto> getMakerById(@PathVariable Long id) throws InternalServiceException {
        return ResponseEntity.ok(makerService.findMakerById(id));
    }
}
