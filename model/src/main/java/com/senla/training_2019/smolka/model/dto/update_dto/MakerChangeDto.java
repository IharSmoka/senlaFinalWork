package com.senla.training_2019.smolka.model.dto.update_dto;

import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public class MakerChangeDto {

    @Null(groups = {CreateMode.class})
    @NotNull(groups = {UpdateMode.class})
    private Long id;

    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    private Integer countryId;

    @NotEmpty(groups = {CreateMode.class, UpdateMode.class})
    @Size(min = 6, max = 50, groups = {CreateMode.class, UpdateMode.class})
    private String makerName;

    public MakerChangeDto() {
    }

    public MakerChangeDto(Long id, Integer countryId, String makerName) {
        this.id = id;
        this.countryId = countryId;
        this.makerName = makerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getMakerName() {
        return makerName;
    }

    public void setMakerName(String makerName) {
        this.makerName = makerName;
    }
}
