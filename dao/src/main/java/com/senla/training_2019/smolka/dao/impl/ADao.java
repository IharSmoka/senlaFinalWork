package com.senla.training_2019.smolka.dao.impl;

import com.senla.training_2019.smolka.api.dao.IDao;
import com.senla.training_2019.smolka.api.exceptions.dao.DataAccessException;
import com.senla.training_2019.smolka.utils.enums.EntityGraphType;
import com.senla.training_2019.smolka.utils.enums.SortOrder;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SearchInstructionType;
import com.senla.training_2019.smolka.utils.join_container.JoinContainer;
import com.senla.training_2019.smolka.utils.pagination_settings.PaginationSettings;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.*;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class ADao<E, Id> implements IDao<E, Id> {

    protected static final String GRAPH_HINT_KEY = "javax.persistence.fetchgraph";
    private static final String UNKNOWN_ENTITY_GRAPH_TYPE = "Unknown entity graph type!";
    private static final String UNKNOWN_SORT_ORDER = "Unknown sort order!";
    private static final String NULL_PAGINATION_SETTINGS = "Sorting settings not initialize!";
    private static final String NULL_FILTER_SETTINGS = "Nothing to search!";

    @PersistenceContext(unitName = "persistence")
    protected EntityManager entityManager;

    public Class<E> getEntityClass() {
        return ((Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    protected EntityGraph<E> createExtendedEntityGraph() {
        return entityManager.createEntityGraph(getEntityClass());
    }

    protected EntityGraph<E> createSimpleEntityGraph() {
        return entityManager.createEntityGraph(getEntityClass());
    }

    protected final EntityGraph<E> getEntityGraph(EntityGraphType entityGraphType) {
        switch (entityGraphType) {
            case SIMPLE: {
                return createSimpleEntityGraph();
            }
            case EXTENDED: {
                return createExtendedEntityGraph();
            }
            default: {
                throw new IllegalArgumentException(UNKNOWN_ENTITY_GRAPH_TYPE);
            }
        }
    }

    @Override
    public E save(E entity) throws DataAccessException {
        try {
            entityManager.persist(entity);
            return entity;
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public void delete(Id id) throws DataAccessException {
        try {
            E instance = findById(EntityGraphType.SIMPLE, id);
            entityManager.remove(instance);
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public E update(E entity) throws DataAccessException {
        try {
            entityManager.merge(entity);
            return entity;
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    private boolean joinIsRequired(Attribute<?, ?> attr) {
        return !attr.getDeclaringType().getJavaType().equals(getEntityClass());
    }

    private Order createOrderByRoot(CriteriaBuilder criteriaBuilder, SortOrder order, Root<?> root, SingularAttribute<?, ?> attr) {
        switch (order) {
            case ASC: {
                return criteriaBuilder.asc(root.get(attr.getName()));
            }
            case DESC: {
                return criteriaBuilder.desc(root.get(attr.getName()));
            }
            default: {
                throw new IllegalArgumentException(UNKNOWN_SORT_ORDER);
            }
        }
    }

    private Order createOrderByJoin(CriteriaBuilder criteriaBuilder, SortOrder order, Join<?, ?> join, SingularAttribute<?, ?> attr) {
        switch (order) {
            case ASC: {
                return criteriaBuilder.asc(join.get(attr.getName()));
            }
            case DESC: {
                return criteriaBuilder.desc(join.get(attr.getName()));
            }
            default: {
                throw new IllegalArgumentException(UNKNOWN_SORT_ORDER);
            }
        }
    }

    protected <Q> Order getOrder(CriteriaBuilder criteriaBuilder, Root<Q> root, PaginationSettings paginationSettings, JoinContainer<Q> joinContainer) {
        if (!paginationSettings.withSort()) {
            throw new IllegalArgumentException(NULL_PAGINATION_SETTINGS);
        }
        SingularAttribute<?, ?> sortAttr = paginationSettings.getSortAttribute();
        if (joinIsRequired(sortAttr) && paginationSettings.getJoinAttr() != null) {
            if (joinContainer != null) {
                if (joinContainer.getJoin(sortAttr) != null) {
                    return createOrderByJoin(criteriaBuilder, paginationSettings.getSortOrder(), joinContainer.getJoin(sortAttr), sortAttr);
                } else {
                    return createOrderByJoin(criteriaBuilder, paginationSettings.getSortOrder(), joinContainer.createJoin(paginationSettings.getJoinAttr(), root), sortAttr);
                }
            }
            return createOrderByJoin(criteriaBuilder, paginationSettings.getSortOrder(), root.join(paginationSettings.getJoinAttr().getName()), sortAttr);
        } else {
            return createOrderByRoot(criteriaBuilder, paginationSettings.getSortOrder(), root, sortAttr);
        }
    }

    protected <Q> Predicate createPredicateByFilterSettings(SingularAttributeFilterSettings filterSettings, JoinContainer<Q> joinContainer, CriteriaBuilder builder, Root<Q> root) {
        List<Predicate> predicates = new ArrayList<>();
        Set<SingularAttribute<?, ?>> attributes = filterSettings.getAttributes();
        if (joinContainer == null) {
            joinContainer = new JoinContainer<>();
        }
        joinContainer.createJoinsForFilterSettings(filterSettings, root);
        for (Attribute<?, ?> attr : attributes) {
            SearchInstructionType searchType = filterSettings.getFilterVal(attr).getSearchInstructionType();
            if (joinIsRequired(attr)) {
                switch (searchType) {
                    case LIKE: {
                        predicates.add(builder.like(joinContainer.getJoin(attr).get(attr.getName()), "%" + (String) filterSettings.getFilterVal(attr).getVal() + "%"));
                        break;
                    }
                    case EQUAL: {
                        predicates.add(builder.equal(joinContainer.getJoin(attr).get(attr.getName()), filterSettings.getFilterVal(attr).getVal()));
                        break;
                    }
                }
            } else {
                switch (searchType) {
                    case LIKE: {
                        predicates.add(builder.like(root.get(attr.getName()), "%" + (String) filterSettings.getFilterVal(attr).getVal() + "%"));
                        break;
                    }
                    case EQUAL: {
                        predicates.add(builder.equal(root.get(attr.getName()), filterSettings.getFilterVal(attr).getVal()));
                        break;
                    }
                }
            }
        }
        return builder.and(predicates.toArray(Predicate[]::new));
    }

    @Override
    public E findById(EntityGraphType entityGraphType, Id id) throws DataAccessException {
        try {
            Map<String, Object> properties = new HashMap<>();
            properties.put(GRAPH_HINT_KEY, getEntityGraph(entityGraphType));
            return entityManager.find(getEntityClass(), id, properties);
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public E findByAttribute(EntityGraphType entityGraphType, PaginationSettings paginationSettings, SingularAttributeFilterSettings singularAttributeFilterSettings) throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<E> query = builder.createQuery(getEntityClass());
            Root<E> root = query.from(getEntityClass());
            query.select(root);
            JoinContainer<E> joinContainer = new JoinContainer<>();
            Predicate predicate = createPredicateByFilterSettings(singularAttributeFilterSettings, joinContainer, builder, root);
            query.where(predicate);
            if (paginationSettings == null) {
                List<E> list = entityManager.createQuery(query).setHint(GRAPH_HINT_KEY, getEntityGraph(entityGraphType)).getResultList();
                if (list == null || list.isEmpty()) {
                    return null;
                }
                return list.get(0);
            }
            TypedQuery<E> typedQuery = entityManager.createQuery(query)
                    .setHint(GRAPH_HINT_KEY, getEntityGraph(entityGraphType));
            if (paginationSettings.withPagination()) {
                typedQuery.setFirstResult(paginationSettings.getOffset())
                        .setMaxResults(paginationSettings.getLimit());
            }
            List<E> list = entityManager.createQuery(query).setHint(GRAPH_HINT_KEY, getEntityGraph(entityGraphType)).getResultList();
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list.get(0);
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public List<E> findAllWithFilterSettings(EntityGraphType graphType, PaginationSettings paginationSettings, SingularAttributeFilterSettings singularAttributeFilterSettings) throws DataAccessException {
        try {
            if (singularAttributeFilterSettings == null || singularAttributeFilterSettings.isEmpty()) {
                throw new DataAccessException(NULL_FILTER_SETTINGS);
            }
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<E> query = builder.createQuery(getEntityClass());
            Root<E> root = query.from(getEntityClass());
            query.select(root);
            JoinContainer<E> joinContainer = new JoinContainer<>();
            Predicate predicate = createPredicateByFilterSettings(singularAttributeFilterSettings, joinContainer, builder, root);
            query.where(predicate);
            if (paginationSettings == null) {
                return entityManager.createQuery(query)
                        .setHint(GRAPH_HINT_KEY, getEntityGraph(graphType)).getResultList();
            }
            if (paginationSettings.withSort()) {
                query.orderBy(getOrder(builder, root, paginationSettings, joinContainer));
            }
            TypedQuery<E> typedQuery = entityManager.createQuery(query).setHint(GRAPH_HINT_KEY, getEntityGraph(graphType));
            if (paginationSettings.withPagination()) {
                typedQuery.setFirstResult(paginationSettings.getOffset())
                        .setMaxResults(paginationSettings.getLimit());
            }
            return typedQuery.getResultList();
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public List<E> findAll(EntityGraphType graphType, PaginationSettings paginationSettings) throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<E> query = builder.createQuery(getEntityClass());
            Root<E> root = query.from(getEntityClass());
            query.select(root);
            if (paginationSettings == null) {
                return entityManager.createQuery(query)
                        .setHint(GRAPH_HINT_KEY, getEntityGraph(graphType))
                        .getResultList();
            }
            if (paginationSettings.withSort()) {
                query.orderBy(getOrder(builder, root, paginationSettings, null));
            }
            TypedQuery<E> typedQuery = entityManager.createQuery(query)
                    .setHint(GRAPH_HINT_KEY, getEntityGraph(graphType));
            if (paginationSettings.withPagination()) {
                typedQuery.setFirstResult(paginationSettings.getOffset())
                        .setMaxResults(paginationSettings.getLimit());
            }
            return typedQuery.getResultList();
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    @Override
    public Long getCount() throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<E> root = query.from(getEntityClass());
            query.select(builder.count(root));
            return entityManager.createQuery(query).getSingleResult();
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }

    public Long getCountByFilterSettings(SingularAttributeFilterSettings filterSettings) throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<E> root = query.from(getEntityClass());
            query.select(builder.count(root));
            JoinContainer<E> joinContainer = new JoinContainer<>();
            Predicate predicate = createPredicateByFilterSettings(filterSettings, joinContainer, builder, root);
            query.where(predicate);
            return entityManager.createQuery(query).getSingleResult();
        }
        catch (PersistenceException hibernateExc) {
            throw new DataAccessException(hibernateExc.getMessage());
        }
    }
}
