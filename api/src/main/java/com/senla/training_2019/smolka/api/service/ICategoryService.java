package com.senla.training_2019.smolka.api.service;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.model.dto.CategoryDto;
import com.senla.training_2019.smolka.model.entities.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDto category) throws InternalServiceException;
    Category updateCategory(CategoryDto category) throws InternalServiceException, EntityNotFoundException;
    void deleteCategory(Integer id) throws InternalServiceException, EntityNotFoundException;
    CategoryDto findCategoryById(Integer id) throws InternalServiceException, EntityNotFoundException;
    List<CategoryDto> findCategories(Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    List<CategoryDto> findCategoriesByName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException;
    Long getCountForCategories() throws InternalServiceException;
    Long getCountForCategoriesByName(String name) throws InternalServiceException;
}
