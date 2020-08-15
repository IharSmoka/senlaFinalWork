package com.senla.training_2019.smolka.api.service;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.model.dto.update_dto.ProductChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.ProductExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.ProductSimpleDto;
import com.senla.training_2019.smolka.model.entities.Product;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductChangeDto product) throws InternalServiceException, EntityNotFoundException;
    Product updateProduct(ProductChangeDto product) throws InternalServiceException, EntityNotFoundException;
    void createProductsFromCsvText(String text) throws InternalServiceException, EntityNotFoundException;
    void deleteProduct(Long id) throws InternalServiceException, EntityNotFoundException;
    ProductExtendedDto findProductById(Long id) throws InternalServiceException, EntityNotFoundException;
    List<ProductSimpleDto> findProducts(Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<ProductSimpleDto> findProductsByMakerId(Long makerId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<ProductSimpleDto> findProductsByCategoryId(Integer categoryId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<ProductSimpleDto> findProductsByMakerIdAndCategoryId(Long makerId, Integer categoryId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    Long getCountForProducts() throws InternalServiceException;
    Long getCountForProductsByMakerId(Long makerId) throws InternalServiceException;
    Long getCountForProductsByCategoryId(Integer categoryId) throws InternalServiceException;
    Long getCountForProductsByMakerIdAndCategoryId(Long makerId, Integer categoryId) throws InternalServiceException;
}
