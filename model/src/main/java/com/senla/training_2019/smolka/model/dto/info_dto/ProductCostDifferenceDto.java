package com.senla.training_2019.smolka.model.dto.info_dto;

import com.senla.training_2019.smolka.model.dto.simple.ProductSimpleDto;

public class ProductCostDifferenceDto {
    private ProductSimpleDto product;
    private String costDifference;

    public ProductCostDifferenceDto(ProductSimpleDto product, String costDifference) {
        this.product = product;
        this.costDifference = costDifference;
    }

    public ProductSimpleDto getProduct() {
        return product;
    }

    public void setProduct(ProductSimpleDto product) {
        this.product = product;
    }

    public String getCostDifference() {
        return costDifference;
    }

    public void setCostDifference(String costDifference) {
        this.costDifference = costDifference;
    }
}
