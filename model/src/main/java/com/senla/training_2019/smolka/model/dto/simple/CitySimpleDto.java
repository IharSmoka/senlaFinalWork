package com.senla.training_2019.smolka.model.dto.simple;

public class CitySimpleDto {

    private Integer id;
    private String cityName;

    public CitySimpleDto() {
    }

    public CitySimpleDto(Integer id, String cityName) {
        this.id = id;
        this.cityName = cityName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
