package com.senla.training_2019.smolka.price_monitoring_utils.sort_utils;

import com.senla.training_2019.smolka.model.entities.*;
import com.senla.training_2019.smolka.utils.enums.SortOrder;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;

public enum PriceMonitoringSortKey {

    ADDRESS_NAME_ASC(Address_.streetName, SortOrder.ASC),
    ADDRESS_NAME_DESC(Address_.streetName, SortOrder.DESC),
    CATEGORY_NAME_ASC(Category_.categoryName, SortOrder.ASC),
    CATEGORY_NAME_DESC(Category_.categoryName, SortOrder.DESC),
    CITY_NAME_ASC(City_.cityName, SortOrder.ASC),
    CITY_NAME_DESC(City_.cityName, SortOrder.DESC),
    COST_DYNAMIC_DATE_ASC(CostDynamic_.costDate, SortOrder.ASC),
    COST_DYNAMIC_DATE_DESC(CostDynamic_.costDate, SortOrder.DESC),
    COST_DYNAMIC_COST_ASC(CostDynamic_.cost, SortOrder.ASC),
    COST_DYNAMIC_COST_DESC(CostDynamic_.cost, SortOrder.DESC),
    COUNTRY_NAME_ASC(Country_.countryName, SortOrder.ASC),
    COUNTRY_NAME_DESC(Country_.countryName, SortOrder.DESC),
    CREDENTIAL_LOGIN_ASC(Credential_.username, SortOrder.ASC),
    CREDENTIAL_LOGIN_DESC(Credential_.username, SortOrder.DESC),
    MAKER_NAME_ASC(Maker_.makerName, SortOrder.ASC),
    MAKER_NAME_DESC(Maker_.makerName, SortOrder.DESC),
    PRODUCT_NAME_ASC(Product_.productName, SortOrder.ASC),
    PRODUCT_NAME_DESC(Product_.productName, SortOrder.DESC),
    POSITION_COST_ASC(Position_.costDynamics, CostDynamic_.cost, SortOrder.ASC),
    POSITION_COST_DESC(Position_.costDynamics, CostDynamic_.cost, SortOrder.DESC),
    STORE_NAME_ASC(Store_.storeName, SortOrder.ASC),
    STORE_NAME_DESC(Store_.storeName, SortOrder.DESC),
    USER_INFO_NAME_ASC(UserInfo_.firstName, SortOrder.ASC),
    USER_INFO_NAME_DESC(UserInfo_.firstName, SortOrder.ASC),
    USER_INFO_DATE_ASC(UserInfo_.birthDate, SortOrder.ASC),
    USER_INFO_DATE_DESC(UserInfo_.birthDate, SortOrder.DESC);

    private final SingularAttribute<?, ?> sortAttr;
    private final SortOrder sortOrder;
    private Attribute<?, ?> joinAttr;

    PriceMonitoringSortKey(SingularAttribute<?, ?> sortAttr, SortOrder sortOrder) {
        this.sortAttr = sortAttr;
        this.sortOrder = sortOrder;
    }

    PriceMonitoringSortKey(Attribute<?,?> joinSortAttr, SingularAttribute<?, ?> sortAttr, SortOrder sortOrder) {
        this.sortAttr = sortAttr;
        this.sortOrder = sortOrder;
        this.joinAttr = joinSortAttr;
    }

    public SingularAttribute<?, ?> getSortAttr() {
        return sortAttr;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public Attribute<?, ?> getJoinAttr() {
        return joinAttr;
    }
}
