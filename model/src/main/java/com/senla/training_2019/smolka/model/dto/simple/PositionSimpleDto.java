package com.senla.training_2019.smolka.model.dto.simple;

import com.senla.training_2019.smolka.model.dto.CostDynamicDto;
import com.senla.training_2019.smolka.model.dto.extended.ProductExtendedDto;
import com.senla.training_2019.smolka.model.dto.extended.StoreExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.ProductSimpleDto;
import com.senla.training_2019.smolka.model.dto.simple.StoreSimpleDto;
import com.senla.training_2019.smolka.model.entities.Product;


import java.util.ArrayList;
import java.util.List;

public class PositionSimpleDto {

    private Long id;
    private StoreSimpleDto store;
    private ProductSimpleDto product;
    private List<CostDynamicDto> costDynamics;

    public PositionSimpleDto() {

    }

    public PositionSimpleDto(Long id, StoreSimpleDto store, ProductSimpleDto product) {
        this.id = id;
        this.store = store;
        this.product = product;
        costDynamics = new ArrayList<>();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StoreSimpleDto getStore() {
        return store;
    }

    public void setStore(StoreSimpleDto store) {
        this.store = store;
    }

    public ProductSimpleDto getProduct() {
        return product;
    }

    public void setProduct(ProductSimpleDto product) {
        this.product = product;
    }

    public List<CostDynamicDto> getCostDynamics() {
        return costDynamics;
    }

    public void setCostDynamics(List<CostDynamicDto> costDynamics) {
        this.costDynamics = costDynamics;
    }
}
