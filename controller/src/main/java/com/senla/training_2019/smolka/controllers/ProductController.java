package com.senla.training_2019.smolka.controllers;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IProductService;
import com.senla.training_2019.smolka.model.dto.CollectionDto;
import com.senla.training_2019.smolka.model.dto.MessageDto;
import com.senla.training_2019.smolka.model.dto.update_dto.ProductChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.ProductExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.ProductSimpleDto;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final static String CREATE_IS_SUCCESS = "Product created successfully!";
    private final static String UPDATE_IS_SUCCESS = "Product updated successfully!";
    private final static String DELETE_IS_SUCCESS = "Product removed successfully!";
    private final static String CREATE_FROM_FILE_IS_SUCCESS = "Products from file created successfully!";
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> createProduct(@RequestBody @Validated(CreateMode.class) ProductChangeDto productDto) throws EntityNotFoundException, InternalServiceException {
        productService.createProduct(productDto);
        return ResponseEntity.ok(new MessageDto(CREATE_IS_SUCCESS));
    }

    @PostMapping("/csv")
    public ResponseEntity<MessageDto> createFromFile(@RequestParam("file") MultipartFile file) throws IOException, EntityNotFoundException, InternalServiceException {
        productService.createProductsFromCsvText(new String(file.getBytes()));
        return ResponseEntity.ok(new MessageDto(CREATE_FROM_FILE_IS_SUCCESS));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> updateProduct(@RequestBody @Validated(UpdateMode.class)  ProductChangeDto productDto) throws EntityNotFoundException, InternalServiceException {
        productService.updateProduct(productDto);
        return ResponseEntity.ok(new MessageDto(UPDATE_IS_SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> deleteProduct(@RequestParam(value = "id") Long id) throws InternalServiceException, EntityNotFoundException {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new MessageDto(DELETE_IS_SUCCESS));
    }

    @GetMapping("/count")
    public Long getCountForProducts(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                  @RequestParam(value = "makerId", required = false) Long makerId) throws InternalServiceException {
        if (categoryId == null && makerId == null) {
            return productService.getCountForProducts();
        }
        if (categoryId != null && makerId != null) {
            return productService.getCountForProductsByMakerIdAndCategoryId(makerId, categoryId);
        }
        if (categoryId != null) {
            return productService.getCountForProductsByCategoryId(categoryId);
        }
        return productService.getCountForProductsByMakerId(makerId);
    }

    @GetMapping
    public ResponseEntity<CollectionDto> getAllProducts(@RequestParam(value = "page", required = false) Integer page,
                                                      @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                      @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                                      @RequestParam(value = "makerId", required = false) Long makerId,
                                                        @RequestParam(value = "sortKey", required = false) String sortKey) throws InternalServiceException {
        List<ProductSimpleDto> productDtoList = null;
        Long count = 0L;
        if (categoryId == null && makerId == null) {
            productDtoList = productService.findProducts(page, pageSize, sortKey);
            count = productService.getCountForProducts();
            return ResponseEntity.ok(new CollectionDto(count, productDtoList));
        }
        if (categoryId != null && makerId != null) {
            productDtoList = productService.findProductsByMakerIdAndCategoryId(makerId, categoryId, page, pageSize, sortKey);
            count = productService.getCountForProductsByMakerIdAndCategoryId(makerId, categoryId);
            return ResponseEntity.ok(new CollectionDto(count, productDtoList));
        }
        if (categoryId != null) {
            productDtoList = productService.findProductsByCategoryId(categoryId, page, pageSize, sortKey);
            count = productService.getCountForProductsByCategoryId(categoryId);
            return ResponseEntity.ok(new CollectionDto(count, productDtoList));
        }
        productDtoList = productService.findProductsByMakerId(makerId, page, pageSize, sortKey);
        count = productService.getCountForProductsByMakerId(makerId);
        return ResponseEntity.ok(new CollectionDto(count, productDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductExtendedDto> getProductById(@PathVariable Long id) throws InternalServiceException, EntityNotFoundException {
        return ResponseEntity.ok(productService.findProductById(id));
    }
}
