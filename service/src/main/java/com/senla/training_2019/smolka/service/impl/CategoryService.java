package com.senla.training_2019.smolka.service.impl;

import com.senla.training_2019.smolka.api.dao.ICategoryDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.ICategoryService;
import com.senla.training_2019.smolka.mappers.DtoMapper;
import com.senla.training_2019.smolka.model.dto.CategoryDto;
import com.senla.training_2019.smolka.price_monitoring_utils.sort_utils.PriceMonitoringPaginationSettingsBuilder;
import com.senla.training_2019.smolka.service.impl.exception_messages.EntityNotFoundExceptionMessages;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;
import com.senla.training_2019.smolka.model.entities.Category;
import com.senla.training_2019.smolka.model.entities.Category_;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    private static final Logger logger = LogManager.getLogger(CategoryService.class);
    private final ICategoryDao categoryDao;
    private final DtoMapper dtoMapper;

    @Autowired
    public CategoryService(ICategoryDao categoryDao, DtoMapper dtoMapper) {
        this.categoryDao = categoryDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Category createCategory(CategoryDto categoryDto) throws InternalServiceException {
        try {
            Category category = dtoMapper.mapping(categoryDto, Category.class);
            return categoryDao.save(category);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Category updateCategory(CategoryDto categoryDto) throws InternalServiceException, EntityNotFoundException {
        try {
            if (categoryDao.findById(EntityGraphType.SIMPLE, categoryDto.getId()) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.CATEGORY_NOT_EXIST.getMsg());
            }
            Category category = dtoMapper.mapping(categoryDto, Category.class);
            return categoryDao.update(category);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteCategory(Integer id) throws InternalServiceException, EntityNotFoundException {
        try {
            if (categoryDao.findById(EntityGraphType.SIMPLE, id) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.CATEGORY_NOT_EXIST.getMsg());
            }
            categoryDao.delete(id);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto findCategoryById(Integer id) throws InternalServiceException, EntityNotFoundException {
        try {
            Category category =  categoryDao.findById(EntityGraphType.EXTENDED, id);
            if (category == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.CATEGORY_NOT_EXIST.getMsg());
            }
            return dtoMapper.mapping(category, CategoryDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findCategories(Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            List<Category> categories = categoryDao.findAll(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey));
            return dtoMapper.toListMapping(categories, CategoryDto.class);
        } catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findCategoriesByName(String name, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(Category_.categoryName, name);
            List<Category> categories = categoryDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(categories, CategoryDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForCategories() throws InternalServiceException {
        try {
            return categoryDao.getCount();
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForCategoriesByName(String name) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putToSettingsWithLikeInstruction(Category_.categoryName, name);
            return categoryDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }
}
