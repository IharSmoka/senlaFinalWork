package com.senla.training_2019.smolka.model.dto.extended;

import com.senla.training_2019.smolka.model.dto.CountryDto;
import com.senla.training_2019.smolka.model.entities.Country;

public class MakerExtendedDto {

    private Long id;
    private CountryDto country;
    private String makerName;

    public MakerExtendedDto() {

    }

    public MakerExtendedDto(Long id, CountryDto country, String makerName) {
        this.id = id;
        this.country = country;
        this.makerName = makerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CountryDto getCountry() {
        return country;
    }

    public void setCountry(CountryDto country) {
        this.country = country;
    }

    public String getMakerName() {
        return makerName;
    }

    public void setMakerName(String makerName) {
        this.makerName = makerName;
    }
}
