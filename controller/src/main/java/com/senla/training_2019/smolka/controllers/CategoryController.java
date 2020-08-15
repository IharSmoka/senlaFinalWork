package com.senla.training_2019.smolka.controllers;

import com.mysql.cj.jdbc.result.UpdatableResultSet;
import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.ICategoryService;
import com.senla.training_2019.smolka.model.dto.CategoryDto;
import com.senla.training_2019.smolka.model.dto.CollectionDto;
import com.senla.training_2019.smolka.model.dto.MessageDto;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;
import com.senla.training_2019.smolka.model.entities.Category;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final static String CREATE_IS_SUCCESS = "Category created successfully!";
    private final static String UPDATE_IS_SUCCESS = "Category updated successfully!";
    private final static String DELETE_IS_SUCCESS = "Category removed successfully!";
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> createCategory(@RequestBody @Validated(CreateMode.class) CategoryDto categoryDto) throws InternalServiceException {
        categoryService.createCategory(categoryDto);
        return ResponseEntity.ok(new MessageDto(CREATE_IS_SUCCESS));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> updateCategory(@RequestBody @Validated(UpdateMode.class) CategoryDto categoryDto) throws InternalServiceException, EntityNotFoundException {
        categoryService.updateCategory(categoryDto);
        return ResponseEntity.ok(new MessageDto(UPDATE_IS_SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> deleteCategory(@RequestParam(value = "id") Integer id) throws InternalServiceException, EntityNotFoundException {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new MessageDto(DELETE_IS_SUCCESS));
    }

    @GetMapping("/count")
    public Long getCountForCategories(@RequestParam(value = "name", required = false) String name) throws InternalServiceException {
        return name == null ? categoryService.getCountForCategories() : categoryService.getCountForCategoriesByName(name);
    }

    @GetMapping
    public ResponseEntity<CollectionDto> getAllCategories(@RequestParam(value = "page", required = false) Integer page,
                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                          @RequestParam(value = "name", required = false) String name,
                                                          @RequestParam(value = "sortKey", required = false) String sortKey) throws InternalServiceException {
        List<CategoryDto> categoryDtoList = null;
        Long count = 0L;
        if (name == null) {
            categoryDtoList = categoryService.findCategories(page, pageSize, sortKey);
            count = categoryService.getCountForCategories();
        } else {
            categoryDtoList = categoryService.findCategoriesByName(name, page, pageSize, sortKey);
            count = categoryService.getCountForCategoriesByName(name);
        }
        return ResponseEntity.ok(new CollectionDto(count, categoryDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer id) throws InternalServiceException, EntityNotFoundException {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }
}
