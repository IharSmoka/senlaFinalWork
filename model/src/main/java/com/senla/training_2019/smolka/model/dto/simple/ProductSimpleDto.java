package com.senla.training_2019.smolka.model.dto.simple;

public class ProductSimpleDto {

    private Long id;
    private String productName;

    public ProductSimpleDto() {
    }

    public ProductSimpleDto(Long id, String productName) {
        this.id = id;
        this.productName = productName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
