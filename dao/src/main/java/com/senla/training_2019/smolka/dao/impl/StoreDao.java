package com.senla.training_2019.smolka.dao.impl;

import com.senla.training_2019.smolka.api.dao.IStoreDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.model.entities.*;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.PersistenceException;
import javax.persistence.Subgraph;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class StoreDao extends ADao<Store, Long> implements IStoreDao {
    @Override
    protected EntityGraph<Store> createExtendedEntityGraph() {
        EntityGraph<Store> entityGraph = super.createExtendedEntityGraph();
        entityGraph.addAttributeNodes(Store_.address.getName());
        Subgraph<Address> addressSubGraph = entityGraph.addSubgraph(Store_.address.getName());
        Subgraph<City> cityInAddressSubGraph = addressSubGraph.addSubgraph(Address_.city.getName());
        cityInAddressSubGraph.addAttributeNodes(City_.country.getName());
        return entityGraph;
    }

    @Override
    public List<Store> findStoresByIdArray(EntityGraphType entityGraphType, Long... storeIdArr) throws DataAccessException {
        try {
            if (storeIdArr == null || storeIdArr.length == 0) {
                return null;
            }
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Store> query = builder.createQuery(Store.class);
            Root<Store> root = query.from(getEntityClass());
            query.select(root);
            Predicate predicate = root.get(Store_.id).in(storeIdArr);
            query.where(predicate);
            return entityManager.createQuery(query).setHint(GRAPH_HINT_KEY, getEntityGraph(entityGraphType)).getResultList();
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }
}
