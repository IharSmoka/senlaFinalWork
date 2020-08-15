package com.senla.training_2019.smolka.model.dto.update_dto;

import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public class StoreChangeDto {

    @Null(groups = {CreateMode.class})
    @NotNull(groups = {UpdateMode.class})
    private Long id;

    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    private Long addressId;

    @NotEmpty(groups = {CreateMode.class, UpdateMode.class})
    @Size(min = 6, max = 50, groups = {CreateMode.class, UpdateMode.class})
    private String storeName;

    public StoreChangeDto() {
    }

    public StoreChangeDto(Long id, Long addressId, String storeName) {
        this.id = id;
        this.addressId = addressId;
        this.storeName = storeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
