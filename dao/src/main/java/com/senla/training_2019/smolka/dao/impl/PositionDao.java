package com.senla.training_2019.smolka.dao.impl;

import com.senla.training_2019.smolka.api.dao.IPositionDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.model.entities.*;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;
import com.senla.training_2019.smolka.utils.join_container.JoinContainer;
import com.senla.training_2019.smolka.utils.pagination_settings.PaginationSettings;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class PositionDao extends ADao<Position, Long> implements IPositionDao {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    protected EntityGraph<Position> createExtendedEntityGraph() {
        EntityGraph<Position> entityGraph = super.createExtendedEntityGraph();
        entityGraph.addAttributeNodes(Position_.product.getName(), Position_.store.getName(), Position_.costDynamics.getName());

        Subgraph<Product> productSubGraph = entityGraph.addSubgraph(Position_.product.getName());
        productSubGraph.addAttributeNodes(Product_.category.getName(), Product_.maker.getName());
        Subgraph<Maker> makerSubGraph = productSubGraph.addSubgraph(Product_.maker.getName());
        makerSubGraph.addAttributeNodes(Maker_.country.getName());
        Subgraph<Store> storeSubGraph = entityGraph.addSubgraph(Position_.store.getName());

        storeSubGraph.addAttributeNodes(Store_.address.getName());
        Subgraph<Address> addressSubGraph = storeSubGraph.addSubgraph(Store_.address.getName());
        Subgraph<City> cityInAddressSubGraph = addressSubGraph.addSubgraph(Address_.city.getName());
        cityInAddressSubGraph.addAttributeNodes(City_.country.getName());
        return entityGraph;
    }

    @Override
    protected EntityGraph<Position> createSimpleEntityGraph() {
        EntityGraph<Position> entityGraph = super.createExtendedEntityGraph();
        entityGraph.addAttributeNodes(Position_.product.getName(), Position_.store.getName(), Position_.costDynamics.getName());
        return entityGraph;
    }

    private List<Product> findIntersectProducts(Long... storesId) throws DataAccessException {
        try {
            if (storesId == null || storesId.length == 0) {
                return null;
            }
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();
            Root<Position> root = query.from(getEntityClass());
            query.multiselect(root.get(Position_.product));
            Join<Position, Store> storeJoin = root.join(Position_.store);
            Predicate predicate = storeJoin.get(Store_.id).in(storesId);
            query.where(predicate);
            query.groupBy(root.get(Position_.product));
            query.having(builder.equal(builder.count(root.get(Position_.product)), storesId.length));
            List<Product> products = new ArrayList<>();
            entityManager.createQuery(query).getResultList().forEach(tuple -> {
                products.add(tuple.get(0, Product.class));
            });
            return products;
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public List<Position> findPositionsWithEqualProductsAtStores(EntityGraphType entityGraphType, Long... storesId) throws DataAccessException {
        try {
            if (storesId == null || storesId.length == 0) {
                return null;
            }
            List<Product> intersectProducts = findIntersectProducts(storesId);
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Position> query = builder.createQuery(Position.class);
            Root<Position> root = query.from(getEntityClass());
            query.select(root);
            Join<Position, Store> storeJoin = root.join(Position_.store);
            Predicate storePredicate = storeJoin.get(Store_.id).in(storesId);
            Predicate productPredicate = root.get(Position_.product).in(intersectProducts);
            Predicate predicate = builder.and(storePredicate, productPredicate);
            query.where(predicate);
            return entityManager.createQuery(query).setHint(GRAPH_HINT_KEY, getEntityGraph(entityGraphType)).getResultList();
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public List<Position> findCurrentCostsForPositions(EntityGraphType entityGraphType, Long... positionsId) throws DataAccessException {
        try {
            if (positionsId == null || positionsId.length == 0) {
                return null;
            }
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();
            Root<Position> root = query.from(Position.class);
            Join<Position, CostDynamic> costDynamicJoin = root.join(Position_.costDynamics);
            query.multiselect(costDynamicJoin.get(CostDynamic_.position), builder.max(costDynamicJoin.get(CostDynamic_.costDate.getName())));
            Predicate predicate = root.get(Position_.id).in(positionsId);
            query.where(predicate);
            query.groupBy(costDynamicJoin.get(CostDynamic_.position));
            List<String> positionMaxDateStringList = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            entityManager.createQuery(query).getResultList().forEach(tuple -> {
                positionMaxDateStringList.add(tuple.get(0, Position.class).getId().toString() + simpleDateFormat.format(tuple.get(1, Date.class)));
            });
            CriteriaQuery<Position> typedQuery = builder.createQuery(Position.class);
            root = typedQuery.from(Position.class);
            costDynamicJoin = root.join(Position_.costDynamics);
            typedQuery.select(root);
            Predicate predicateForCurrCost = builder.concat(builder.toString(root.get(Position_.id.getName())), builder.toString(costDynamicJoin.get(CostDynamic_.costDate.getName()))).in(positionMaxDateStringList);
            costDynamicJoin.on(predicateForCurrCost);
            return entityManager.createQuery(typedQuery).setHint(GRAPH_HINT_KEY, getEntityGraph(entityGraphType)).getResultList();
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public List<Position> findAllWithFilterSettings(EntityGraphType graphType, PaginationSettings paginationSettings, SingularAttributeFilterSettings singularAttributeFilterSettings) throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();
            Root<Position> root = query.from(Position.class);
            JoinContainer<Position> joinContainer = new JoinContainer<>();
            Join<Position, CostDynamic> costDynamicJoin = joinContainer.createJoin(Position_.costDynamics, root);
            query.multiselect(costDynamicJoin.get(CostDynamic_.position), builder.max(costDynamicJoin.get(CostDynamic_.costDate.getName())));
            Predicate predicate = createPredicateByFilterSettings(singularAttributeFilterSettings, joinContainer, builder, root);
            query.where(predicate);
            query.groupBy(costDynamicJoin.get(CostDynamic_.position));
            List<String> positionMaxDateStringList = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            entityManager.createQuery(query).getResultList().forEach(tuple -> {
                positionMaxDateStringList.add(tuple.get(0, Position.class).getId().toString() + simpleDateFormat.format(tuple.get(1, Date.class)));
            });
            CriteriaQuery<Position> typedCriteriaQuery = builder.createQuery(Position.class);
            root = typedCriteriaQuery.from(Position.class);
            costDynamicJoin = joinContainer.createJoin(Position_.costDynamics, root);
            Predicate predicateForCurrCost = builder.concat(builder.toString(root.get(Position_.id.getName())), builder.toString(costDynamicJoin.get(CostDynamic_.costDate.getName()))).in(positionMaxDateStringList);
            costDynamicJoin.on(predicateForCurrCost);
            typedCriteriaQuery.select(root);
            if (paginationSettings.withSort()) {
                typedCriteriaQuery.orderBy(getOrder(builder, root, paginationSettings, joinContainer));
            }
            TypedQuery<Position> typedQuery = entityManager.createQuery(typedCriteriaQuery);
            if (paginationSettings.withPagination()) {
                typedQuery.setFirstResult(paginationSettings.getOffset())
                        .setMaxResults(paginationSettings.getLimit());
            }
            return typedQuery.setHint(GRAPH_HINT_KEY, getEntityGraph(graphType)).getResultList();
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public List<Position> findAll(EntityGraphType graphType, PaginationSettings paginationSettings) throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();
            Root<Position> root = query.from(Position.class);
            JoinContainer<Position> joinContainer = new JoinContainer<>();
            Join<Position, CostDynamic> costDynamicJoin = joinContainer.createJoin(Position_.costDynamics, root);
            query.multiselect(costDynamicJoin.get(CostDynamic_.position), builder.max(costDynamicJoin.get(CostDynamic_.costDate.getName())));
            query.groupBy(costDynamicJoin.get(CostDynamic_.position));
            List<String> positionMaxDateStringList = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            entityManager.createQuery(query).getResultList().forEach(tuple -> {
                positionMaxDateStringList.add(tuple.get(0, Position.class).getId().toString() + simpleDateFormat.format(tuple.get(1, Date.class)));
            });
            CriteriaQuery<Position> typedCriteriaQuery = builder.createQuery(Position.class);
            root = typedCriteriaQuery.from(Position.class);
            costDynamicJoin = joinContainer.createJoin(Position_.costDynamics, root);
            Predicate predicateForCurrCost = builder.concat(builder.toString(root.get(Position_.id.getName())), builder.toString(costDynamicJoin.get(CostDynamic_.costDate.getName()))).in(positionMaxDateStringList);
            costDynamicJoin.on(predicateForCurrCost);
            typedCriteriaQuery.select(root);
            if (paginationSettings.withSort()) {
                typedCriteriaQuery.orderBy(getOrder(builder, root, paginationSettings, joinContainer));
            }
            TypedQuery<Position> typedQuery = entityManager.createQuery(typedCriteriaQuery);
            if (paginationSettings.withPagination()) {
                typedQuery.setFirstResult(paginationSettings.getOffset())
                        .setMaxResults(paginationSettings.getLimit());
            }
            return typedQuery.setHint(GRAPH_HINT_KEY, getEntityGraph(graphType)).getResultList();
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }
}
