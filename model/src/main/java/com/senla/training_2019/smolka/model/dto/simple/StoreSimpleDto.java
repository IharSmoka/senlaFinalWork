package com.senla.training_2019.smolka.model.dto.simple;

public class StoreSimpleDto {

    private Long id;
    private String storeName;

    public StoreSimpleDto() {
    }

    public StoreSimpleDto(Long id, String storeName) {
        this.id = id;
        this.storeName = storeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
