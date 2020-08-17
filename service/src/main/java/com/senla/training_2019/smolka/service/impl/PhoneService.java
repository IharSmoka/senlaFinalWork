package com.senla.training_2019.smolka.service.impl;

import com.senla.training_2019.smolka.api.dao.IPhoneRepository;
import com.senla.training_2019.smolka.api.dao.IStoreDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IPhoneService;
import com.senla.training_2019.smolka.mappers.DtoMapper;
import com.senla.training_2019.smolka.model.dto.extended.PhoneExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.PhoneSimpleDto;
import com.senla.training_2019.smolka.model.dto.update_dto.PhoneChangeDto;
import com.senla.training_2019.smolka.model.entities.Phone;
import com.senla.training_2019.smolka.model.entities.Phone_;
import com.senla.training_2019.smolka.model.entities.Store;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

@Service
public class PhoneService implements IPhoneService {

    private static final Logger logger = LogManager.getLogger(PhoneService.class);
    private final IPhoneRepository phoneDao;
    private final IStoreDao storeDao;
    private final DtoMapper dtoMapper;

    private enum SortKeysForPhoneService {
        PHONE_NUMBER_ASC(Sort.Direction.ASC, Phone_.number),
        PHONE_NUMBER_DESC(Sort.Direction.DESC, Phone_.number),
        OPERATOR_ASC(Sort.Direction.ASC, Phone_.operatorCode),
        OPERATOR_DESC(Sort.Direction.DESC, Phone_.operatorCode);

        Sort.Direction direction;
        SingularAttribute<?, ?> attr;

        SortKeysForPhoneService(Sort.Direction direction, SingularAttribute<?, ?> attr) {
            this.direction = direction;
            this.attr = attr;
        }

        public Sort.Direction getDirection() {
            return direction;
        }

        public SingularAttribute<?, ?> getAttr() {
            return attr;
        }
    }

    @Autowired
    public PhoneService(IPhoneRepository phoneDao, IStoreDao storeDao, DtoMapper dtoMapper) {
        this.phoneDao = phoneDao;
        this.storeDao = storeDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createPhone(PhoneChangeDto phoneChangeDto) throws InternalServiceException, EntityNotFoundException {
        try {
            Store store = storeDao.findById(EntityGraphType.SIMPLE, phoneChangeDto.getStoreId());
            if (store == null) {
                throw new EntityNotFoundException("this store doesnt exists!");
            }
            Phone phone = new Phone(store, phoneChangeDto.getOperatorCode(), phoneChangeDto.getNumber());
            phoneDao.save(phone);
        }
        catch (DataAccessException dataAccessException) {
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updatePhone(PhoneChangeDto phoneChangeDto) throws InternalServiceException, EntityNotFoundException {
        try {
            Store store = storeDao.findById(EntityGraphType.SIMPLE, phoneChangeDto.getStoreId());
            if (store == null) {
                throw new EntityNotFoundException("this store doesnt exists!");
            }
            Phone phone = phoneDao.findOne(phoneChangeDto.getId());
            if (phone == null) {
                throw new EntityNotFoundException("this phone doesnt exists!");
            }
            phone.setStore(store);
            phone.setNumber(phoneChangeDto.getNumber());
            phone.setOperatorCode(phoneChangeDto.getOperatorCode());
            phoneDao.save(phone);
        }
        catch (DataAccessException dataAccessException) {
            throw new InternalServiceException(dataAccessException.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deletePhone(Integer id) throws InternalServiceException, EntityNotFoundException {
        try {
            if (!(phoneDao.findOne(id) == null)) {
                throw new EntityNotFoundException("this phone doesnt exists!");
            }
            phoneDao.delete(id);
        }
        catch (RuntimeException runtimeException) {
            throw new InternalServiceException(runtimeException.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhoneSimpleDto> findAllPhones(Integer page, Integer pageSize, String sortKey) {
        SortKeysForPhoneService sortKeysForPhoneService = SortKeysForPhoneService.valueOf(sortKey);
        Page<Phone> phonePage = phoneDao.findAll(new PageRequest(page, pageSize, new Sort(sortKeysForPhoneService.getDirection(), sortKeysForPhoneService.getAttr().getName())));
        return dtoMapper.toListMapping(phonePage.getContent(), PhoneSimpleDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhoneSimpleDto> findAllPhonesByStoreId(Long storeId, Integer page, Integer pageSize, String sortKey) {
        SortKeysForPhoneService sortKeysForPhoneService = SortKeysForPhoneService.valueOf(sortKey);
        Page<Phone> phonePage = phoneDao.findAllByStoreId(storeId, new PageRequest(page, pageSize, new Sort(sortKeysForPhoneService.getDirection(), sortKeysForPhoneService.getAttr().getName())));
        return dtoMapper.toListMapping(phonePage.getContent(), PhoneSimpleDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhoneExtendedDto> findAllPhonesByOperatorCode(String operatorCode, Integer page, Integer pageSize, String sortKey) {
        SortKeysForPhoneService sortKeysForPhoneService = SortKeysForPhoneService.valueOf(sortKey);
        Page<Phone> phonePage = phoneDao.findAllByOperatorCodeLike("%"+operatorCode+"%", new PageRequest(page, pageSize, new Sort(sortKeysForPhoneService.getDirection(), sortKeysForPhoneService.getAttr().getName())));
        return dtoMapper.toListMapping(phonePage.getContent(), PhoneExtendedDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhoneExtendedDto> findAllPhonesByNumber(String number, Integer page, Integer pageSize, String sortKey) {
        SortKeysForPhoneService sortKeysForPhoneService = SortKeysForPhoneService.valueOf(sortKey);
        Page<Phone> phonePage = phoneDao.findAllByNumberLike("%"+number+"%", new PageRequest(page, pageSize, new Sort(sortKeysForPhoneService.getDirection(), sortKeysForPhoneService.getAttr().getName())));
        return dtoMapper.toListMapping(phonePage.getContent(), PhoneExtendedDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountOfPhones() {
        return phoneDao.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountOfPhonesByStoreId(Long storeId) {
        return phoneDao.countByStoreId(storeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountOfPhonesByNumber(String like) {
        return phoneDao.countByNumberLike("%"+like+"%");
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountOfPhonesByOperatorCode(String operator) {
        return phoneDao.countByOperatorCodeLike("%"+operator+"%");
    }
}
