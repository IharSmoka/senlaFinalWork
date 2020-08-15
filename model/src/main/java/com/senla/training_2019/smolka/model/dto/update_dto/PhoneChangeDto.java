package com.senla.training_2019.smolka.model.dto.update_dto;

import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public class PhoneChangeDto {

    @Null(groups = {CreateMode.class})
    @NotNull(groups = {UpdateMode.class})
    private Integer id;

    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    private Long storeId;

    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    @Size(min = 3, max = 6, groups = {CreateMode.class, UpdateMode.class})
    private String operatorCode;

    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    @Size(min = 6, max = 12, groups = {CreateMode.class, UpdateMode.class})
    private String number;

    public PhoneChangeDto() {
    }

    public PhoneChangeDto(Long storeId, String operatorCode, String number) {
        this.storeId = storeId;
        this.operatorCode = operatorCode;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
