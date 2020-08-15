package com.senla.training_2019.smolka.utils.pagination_settings;

import com.senla.training_2019.smolka.utils.enums.SortOrder;
import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;

public class PaginationSettings {
    private SortOrder sortOrder;
    private Attribute<?, ?> joinAttr;
    private SingularAttribute<?, ?> sortAttribute;
    private Integer limit;
    private Integer offset;

    public PaginationSettings(SortOrder sortOrder, SingularAttribute<?, ?> sortAttribute) {
        this.sortOrder = sortOrder;
        this.sortAttribute = sortAttribute;
    }

    public PaginationSettings(Integer limit, Integer offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public PaginationSettings(SortOrder sortOrder,  SingularAttribute<?, ?> sortAttribute, Integer limit, Integer offset) {
        this.sortOrder = sortOrder;
        this.sortAttribute = sortAttribute;
        this.limit = limit;
        this.offset = offset;
    }

    public PaginationSettings(SortOrder sortOrder, Attribute<?, ?> joinAttr, SingularAttribute<?, ?> sortAttribute, Integer limit, Integer offset) {
        this.sortOrder = sortOrder;
        this.sortAttribute = sortAttribute;
        this.limit = limit;
        this.offset = offset;
        this.joinAttr = joinAttr;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public SingularAttribute<?, ?> getSortAttribute() {
        return sortAttribute;
    }

    public boolean withPagination() {
        return offset != null && limit != null;
    }

    public boolean withSort() {
        return sortAttribute != null && sortOrder != null;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public Attribute<?, ?> getJoinAttr() {
        return joinAttr;
    }
}
