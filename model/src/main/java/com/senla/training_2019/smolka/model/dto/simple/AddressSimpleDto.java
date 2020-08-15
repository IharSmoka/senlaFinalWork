package com.senla.training_2019.smolka.model.dto.simple;

public class AddressSimpleDto {

    private Long id;
    private String streetName;

    public AddressSimpleDto() {

    }

    public AddressSimpleDto(Long id, String streetName) {
        this.id = id;
        this.streetName = streetName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
