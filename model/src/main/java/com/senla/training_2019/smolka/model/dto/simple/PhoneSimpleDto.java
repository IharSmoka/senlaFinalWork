package com.senla.training_2019.smolka.model.dto.simple;

import com.senla.training_2019.smolka.model.entities.Store;

import javax.persistence.*;

public class PhoneSimpleDto {

    private Integer id;

    private String operatorCode;

    private String number;

    public PhoneSimpleDto() {
    }

    public PhoneSimpleDto(String operatorCode, String number) {
        this.operatorCode = operatorCode;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
