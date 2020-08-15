package com.senla.training_2019.smolka.dao.impl;

import com.senla.training_2019.smolka.api.dao.ICostDynamicDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.model.entities.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.PersistenceException;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.*;

@Repository
public class CostDynamicDao extends ADao<CostDynamic, Long> implements ICostDynamicDao {

    @Override
    protected EntityGraph<CostDynamic> createExtendedEntityGraph() {
        EntityGraph<CostDynamic> entityGraph = super.createExtendedEntityGraph();
        entityGraph.addAttributeNodes(CostDynamic_.position.getName());
        return entityGraph;
    }

    @Override
    public Long findCurrentCostByProductAtStore(Product product, Store store) throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();
            Root<Position> root = query.from(Position.class);
            Join<Position, CostDynamic> positionCostDynamicJoin = root.join(Position_.costDynamics);
            positionCostDynamicJoin.on(builder.equal(root.get(Position_.id), positionCostDynamicJoin.get(CostDynamic_.position)));
            query.multiselect(positionCostDynamicJoin.get(CostDynamic_.cost));
            query.where(builder.equal(root.get(Position_.product), product), builder.equal(root.get(Position_.store), store));
            query.orderBy(builder.desc(positionCostDynamicJoin.get(CostDynamic_.costDate)));
            List<Tuple> tuples = entityManager.createQuery(query).setMaxResults(1).getResultList();
            if (tuples.isEmpty()) {
                return null;
            }
            return tuples.get(0).get(0, Long.class);
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public List<CostDynamic> findByProductAndStoreBetweenDates(EntityGraphType entityGraphType, Product product, Store store, Date date1, Date date2) throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<CostDynamic> query = builder.createQuery(CostDynamic.class);
            Root<CostDynamic> root = query.from(CostDynamic.class);
            Join<CostDynamic, Position> costDynamicPositionJoin = root.join(CostDynamic_.position);
            query.select(root);
            query.where(builder.equal(costDynamicPositionJoin.get(Position_.product), product), builder.equal(costDynamicPositionJoin.get(Position_.store), store), builder.between(root.<Date>get(CostDynamic_.costDate), date1, date2));
            return entityManager.createQuery(query).setHint(GRAPH_HINT_KEY, getEntityGraph(entityGraphType)).getResultList();
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public List<CostDynamic> findByProduct(EntityGraphType entityGraphType, Product product) throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<CostDynamic> query = builder.createQuery(CostDynamic.class);
            Root<CostDynamic> root = query.from(CostDynamic.class);
            Join<CostDynamic, Position> costDynamicPositionJoin = root.join(CostDynamic_.position);
            query.select(root);
            query.where(builder.equal(costDynamicPositionJoin.get(Position_.product), product));
            return entityManager.createQuery(query).setHint(GRAPH_HINT_KEY, getEntityGraph(entityGraphType)).getResultList();
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public List<CostDynamic> findByPositionsBetweenDates(EntityGraphType entityGraphType, Date date1, Date date2, Position... positions) throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<CostDynamic> query = builder.createQuery(getEntityClass());
            Root<CostDynamic> root = query.from(getEntityClass());
            query.select(root);
            query.where(builder.and(builder.greaterThanOrEqualTo(root.<Date>get(CostDynamic_.costDate.getName()), date1), builder.lessThanOrEqualTo(root.<Date>get(CostDynamic_.costDate.getName()), date2)), root.get(CostDynamic_.position).in(positions));
            return entityManager.createQuery(query).setHint(GRAPH_HINT_KEY, getEntityGraph(entityGraphType)).getResultList();
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public CostDynamic findCurrentCostByPosition(EntityGraphType entityGraphType, Position position) throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<CostDynamic> query = builder.createQuery(getEntityClass());
            Root<CostDynamic> root = query.from(getEntityClass());
            query.select(root);
            query.where(builder.equal(root.get(CostDynamic_.position), position));
            query.orderBy(builder.desc(root.get(CostDynamic_.costDate)));
            List<CostDynamic> costDynamics = entityManager.createQuery(query).setHint(GRAPH_HINT_KEY, getEntityGraph(entityGraphType)).setMaxResults(1).getResultList();
            if (costDynamics == null || costDynamics.isEmpty()) {
                return null;
            }
            return costDynamics.get(0);
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }
}
