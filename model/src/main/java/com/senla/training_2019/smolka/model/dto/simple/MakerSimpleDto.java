package com.senla.training_2019.smolka.model.dto.simple;

public class MakerSimpleDto {

    private Long id;
    private String makerName;

    public MakerSimpleDto() {
    }

    public MakerSimpleDto(Long id, String makerName) {
        this.id = id;
        this.makerName = makerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMakerName() {
        return makerName;
    }

    public void setMakerName(String makerName) {
        this.makerName = makerName;
    }
}
