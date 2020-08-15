package com.senla.training_2019.smolka.model.dto.info_dto;

import com.senla.training_2019.smolka.model.dto.extended.StoreExtendedDto;
import com.senla.training_2019.smolka.model.dto.simple.ProductSimpleDto;

import java.util.ArrayList;
import java.util.List;

public class StoreCostDifferenceDto {
    private StoreExtendedDto firstStore;
    private StoreExtendedDto secondStore;
    private final List<ProductCostDifferenceDto> productCostDifference = new ArrayList<>();

    public StoreCostDifferenceDto(StoreExtendedDto firstStore, StoreExtendedDto secondStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
    }

    public StoreExtendedDto getFirstStore() {
        return firstStore;
    }

    public void setFirstStore(StoreExtendedDto firstStore) {
        this.firstStore = firstStore;
    }

    public StoreExtendedDto getSecondStore() {
        return secondStore;
    }

    public void setSecondStore(StoreExtendedDto secondStore) {
        this.secondStore = secondStore;
    }

    public void addProductCostDifference(ProductSimpleDto productDto, String diff) {
        productCostDifference.add(new ProductCostDifferenceDto(productDto, diff));
    }

    public List<ProductCostDifferenceDto> getProductCostDifference() {
        return productCostDifference;
    }
}
