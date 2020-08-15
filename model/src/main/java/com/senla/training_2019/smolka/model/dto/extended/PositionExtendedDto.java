package com.senla.training_2019.smolka.model.dto.extended;

import com.senla.training_2019.smolka.model.dto.CostDynamicDto;
import com.senla.training_2019.smolka.model.dto.simple.ProductSimpleDto;
import com.senla.training_2019.smolka.model.dto.simple.StoreSimpleDto;

import java.util.ArrayList;
import java.util.List;

public class PositionExtendedDto {

    private Long id;
    private StoreExtendedDto store;
    private ProductExtendedDto product;
    private List<CostDynamicDto> costDynamics;

    public PositionExtendedDto() {

    }

    public PositionExtendedDto(Long id, StoreExtendedDto store, ProductExtendedDto product) {
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

    public StoreExtendedDto getStore() {
        return store;
    }

    public void setStore(StoreExtendedDto store) {
        this.store = store;
    }

    public ProductExtendedDto getProduct() {
        return product;
    }

    public void setProduct(ProductExtendedDto product) {
        this.product = product;
    }

    public List<CostDynamicDto> getCostDynamics() {
        return costDynamics;
    }

    public void setCostDynamics(List<CostDynamicDto> costDynamics) {
        this.costDynamics = costDynamics;
    }
}
