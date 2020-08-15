package com.senla.training_2019.smolka.model.dto.update_dto;

import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public class CityChangeDto {

    @Null(groups = {CreateMode.class})
    @NotNull(groups = {UpdateMode.class})
    private Integer id;

    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    private Integer countryId;

    @NotEmpty(groups = {CreateMode.class, UpdateMode.class})
    @Size(min = 4, max = 50, groups = {CreateMode.class, UpdateMode.class})
    private String cityName;

    public CityChangeDto() {
    }

    public CityChangeDto(Integer id, Integer countryId, String cityName) {
        this.id = id;
        this.countryId = countryId;
        this.cityName = cityName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
