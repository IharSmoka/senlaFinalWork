package com.senla.training_2019.smolka.model.dto.extended;

public class AddressExtendedDto {

    private Long id;
    private CityExtendedDto city;
    private String streetName;

    public AddressExtendedDto() {

    }

    public AddressExtendedDto(Long id, CityExtendedDto city, String streetName) {
        this.id = id;
        this.city = city;
        this.streetName = streetName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CityExtendedDto getCity() {
        return city;
    }

    public void setCity(CityExtendedDto city) {
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
