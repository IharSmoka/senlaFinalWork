package com.senla.training_2019.smolka.model.dto.extended;

import com.senla.training_2019.smolka.model.dto.CountryDto;

public class CityExtendedDto {

    private Integer id;
    private CountryDto country;
    private String cityName;

    public CityExtendedDto() {

    }

    public CityExtendedDto(Integer id, CountryDto country, String cityName) {
        this.id = id;
        this.country = country;
        this.cityName = cityName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CountryDto getCountry() {
        return country;
    }

    public void setCountry(CountryDto country) {
        this.country = country;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
