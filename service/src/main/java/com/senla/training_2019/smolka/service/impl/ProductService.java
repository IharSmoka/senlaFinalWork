package com.senla.training_2019.smolka.service.impl;

import com.senla.training_2019.smolka.api.dao.ICategoryDao;
import com.senla.training_2019.smolka.api.dao.IMakerDao;
import com.senla.training_2019.smolka.api.dao.IProductDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IProductService;
import com.senla.training_2019.smolka.mappers.DtoMapper;
import com.senla.training_2019.smolka.model.dto.update_dto.ProductChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.ProductExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.ProductSimpleDto;
import com.senla.training_2019.smolka.model.entities.*;
import com.senla.training_2019.smolka.price_monitoring_utils.csv_parser.CsvParserException;
import com.senla.training_2019.smolka.price_monitoring_utils.csv_parser.PriceMonitoringUtils;
import com.senla.training_2019.smolka.price_monitoring_utils.sort_utils.PriceMonitoringPaginationSettingsBuilder;
import com.senla.training_2019.smolka.service.impl.exception_messages.EntityNotFoundExceptionMessages;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService implements IProductService {

    private static final Logger logger = LogManager.getLogger(ProductService.class);
    private final IProductDao productDao;
    private final IMakerDao makerDao;
    private final ICategoryDao categoryDao;
    private final DtoMapper dtoMapper;

    @Autowired
    public ProductService(IProductDao productDao, IMakerDao makerDao, ICategoryDao categoryDao, DtoMapper dtoMapper) {
        this.productDao = productDao;
        this.makerDao = makerDao;
        this.categoryDao = categoryDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Product createProduct(ProductChangeDto productDto) throws InternalServiceException, EntityNotFoundException {
        try {
            Product product = dtoMapper.mapping(productDto, Product.class);
            Maker maker = makerDao.findById(EntityGraphType.SIMPLE, productDto.getMakerId());
            if (maker == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.MAKER_NOT_EXIST.getMsg());
            }
            Category category = categoryDao.findById(EntityGraphType.SIMPLE, productDto.getCategoryId());
            if (category == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.CATEGORY_NOT_EXIST.getMsg());
            }
            product.setCategory(category);
            product.setMaker(maker);
            return productDao.save(product);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Product updateProduct(ProductChangeDto productDto) throws InternalServiceException, EntityNotFoundException {
        try {
            if (productDao.findById(EntityGraphType.SIMPLE, productDto.getId()) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.PRODUCT_NOT_EXIST.getMsg());
            }
            Product product = dtoMapper.mapping(productDto, Product.class);
            Maker maker = makerDao.findById(EntityGraphType.SIMPLE, productDto.getMakerId());
            if (maker == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.MAKER_NOT_EXIST.getMsg());
            }
            Category category = categoryDao.findById(EntityGraphType.SIMPLE, productDto.getCategoryId());
            if (category == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.CATEGORY_NOT_EXIST.getMsg());
            }
            product.setCategory(category);
            product.setMaker(maker);
            return productDao.update(product);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createProductsFromCsvText(String text) throws InternalServiceException, EntityNotFoundException {
        try {
            List<ProductChangeDto> newProducts = PriceMonitoringUtils.getProductDtoListFromCsvText(text);
            for (ProductChangeDto productDto : newProducts) {
                Product product = dtoMapper.mapping(productDto, Product.class);
                Maker maker = makerDao.findById(EntityGraphType.SIMPLE, productDto.getMakerId());
                if (maker == null) {
                    throw new EntityNotFoundException(EntityNotFoundExceptionMessages.MAKER_NOT_EXIST.getMsg());
                }
                Category category = categoryDao.findById(EntityGraphType.SIMPLE, productDto.getCategoryId());
                if (category == null) {
                    throw new EntityNotFoundException(EntityNotFoundExceptionMessages.CATEGORY_NOT_EXIST.getMsg());
                }
                product.setCategory(category);
                product.setMaker(maker);
                productDao.save(product);
            }
        }
        catch (CsvParserException | DataAccessException exception) {
            logger.error(exception.getMessage(), exception);
            throw new InternalServiceException(exception.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteProduct(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            if (productDao.findById(EntityGraphType.SIMPLE, id) == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.PRODUCT_NOT_EXIST.getMsg());
            }
            productDao.delete(id);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    public ProductExtendedDto findProductById(Long id) throws InternalServiceException, EntityNotFoundException {
        try {
            Product product = productDao.findById(EntityGraphType.EXTENDED, id);
            if (product == null) {
                throw new EntityNotFoundException(EntityNotFoundExceptionMessages.PRODUCT_NOT_EXIST.getMsg());
            }
            return dtoMapper.mapping(product, ProductExtendedDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSimpleDto> findProducts(Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            List<Product> products = productDao.findAll(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey));
            return dtoMapper.toListMapping(products, ProductSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSimpleDto> findProductsByMakerId(Long makerId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Product_.maker, Maker_.id, makerId);
            List<Product> products = productDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(products, ProductSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    public List<ProductSimpleDto> findProductsByCategoryId(Integer categoryId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Product_.category, Category_.id, categoryId);
            List<Product> products = productDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(products, ProductSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    public List<ProductSimpleDto> findProductsByMakerIdAndCategoryId(Long makerId, Integer categoryId, Integer page, Integer pageSize, String sortKey) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Product_.maker, Maker_.id, makerId)
                    .putJoinToSettingsWithEqualInstruction(Product_.category, Category_.id, categoryId);
            List<Product> products = productDao.findAllWithFilterSettings(EntityGraphType.SIMPLE, PriceMonitoringPaginationSettingsBuilder.buildPaginationSettings(page, pageSize, sortKey), filterSettings);
            return dtoMapper.toListMapping(products, ProductSimpleDto.class);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForProducts() throws InternalServiceException {
        try {
            return productDao.getCount();
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForProductsByMakerId(Long makerId) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Product_.maker, Maker_.id, makerId);
            return productDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    public Long getCountForProductsByCategoryId(Integer categoryId) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Product_.category, Category_.id, categoryId);
            return productDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    public Long getCountForProductsByMakerIdAndCategoryId(Long makerId, Integer categoryId) throws InternalServiceException {
        try {
            SingularAttributeFilterSettings filterSettings = new SingularAttributeFilterSettings();
            filterSettings.putJoinToSettingsWithEqualInstruction(Product_.maker, Maker_.id, makerId)
                    .putJoinToSettingsWithEqualInstruction(Product_.category, Category_.id, categoryId);
            return productDao.getCountByFilterSettings(filterSettings);
        }
        catch (DataAccessException dataAccessException) {
            logger.error(dataAccessException.getMessage(), dataAccessException);
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }
}
