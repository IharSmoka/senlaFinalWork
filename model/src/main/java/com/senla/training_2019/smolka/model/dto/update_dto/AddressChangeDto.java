package com.senla.training_2019.smolka.model.dto.update_dto;

import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public class AddressChangeDto {

    @Null(groups = {CreateMode.class})
    @NotNull(groups = {UpdateMode.class})
    private Long id;

    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    private Integer cityId;

    @NotEmpty(groups = {CreateMode.class, UpdateMode.class})
    @Size(min = 10, max = 128, groups = {CreateMode.class, UpdateMode.class})
    private String streetName;

    public AddressChangeDto() {
    }

    public AddressChangeDto(Long id, Integer cityId, String streetName) {
        this.id = id;
        this.cityId = cityId;
        this.streetName = streetName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
