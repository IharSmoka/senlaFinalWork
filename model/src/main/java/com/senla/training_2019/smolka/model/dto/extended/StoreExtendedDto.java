package com.senla.training_2019.smolka.model.dto.extended;

import com.senla.training_2019.smolka.model.dto.simple.AddressSimpleDto;

public class StoreExtendedDto {

    private Long id;
    private AddressExtendedDto address;
    private String storeName;

    public StoreExtendedDto() {

    }

    public StoreExtendedDto(Long id, AddressExtendedDto address, String storeName) {
        this.id = id;
        this.address = address;
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

    public AddressExtendedDto getAddress() {
        return address;
    }

    public void setAddress(AddressExtendedDto address) {
        this.address = address;
    }
}
