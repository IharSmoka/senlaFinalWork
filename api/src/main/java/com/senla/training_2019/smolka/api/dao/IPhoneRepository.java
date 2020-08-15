package com.senla.training_2019.smolka.api.dao;

import com.senla.training_2019.smolka.model.entities.Phone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IPhoneRepository extends PagingAndSortingRepository<Phone, Integer> {

    @EntityGraph(value = "Phone.store")
    Page<Phone> findAllByNumberLike(String numberLike, Pageable pageable);

    @EntityGraph(attributePaths = { "store", "store.address", "store.address.city" })
    Page<Phone> findAllByOperatorCodeLike(String operatorCodeLike, Pageable pageable);

    @Query(value = "SELECT * FROM phone WHERE store_id = ?1 ORDER BY ?#{#pageable}",
            countQuery = "SELECT count(*) FROM phone WHERE store_id = ?1",
            nativeQuery = true)
    Page<Phone> findAllByStoreId(Long id, Pageable pageable);

    Long countByNumberLike(String numberLike);

    Long countByOperatorCodeLike(String numberLike);

    @Query(value = "SELECT count(*) FROM phone WHERE store_id = ?1", nativeQuery = true)
    Long countByStoreId(Long id);
}
