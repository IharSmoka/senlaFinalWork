package com.senla.training_2019.smolka.model.dto.extended;

import com.senla.training_2019.smolka.model.dto.simple.StoreSimpleDto;

public class PhoneExtendedDto {

    private Integer id;

    private StoreSimpleDto store;

    private String operatorCode;

    private String number;

    public PhoneExtendedDto() {
    }

    public PhoneExtendedDto(StoreSimpleDto store, String operatorCode, String number) {
        this.store = store;
        this.operatorCode = operatorCode;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StoreSimpleDto getStore() {
        return store;
    }

    public void setStore(StoreSimpleDto store) {
        this.store = store;
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
