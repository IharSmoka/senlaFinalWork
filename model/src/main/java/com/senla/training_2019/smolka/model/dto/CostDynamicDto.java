package com.senla.training_2019.smolka.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

public class CostDynamicDto {

    @Null(groups = {CreateMode.class})
    @NotNull(groups = {UpdateMode.class})
    private Long id;

    @NotEmpty(groups = {CreateMode.class, UpdateMode.class})
    private String cost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    private Date costDate;

    public CostDynamicDto() {
    }

    public CostDynamicDto(Long id, String cost, Date costDate) {
        this.id = id;
        this.cost = cost;
        this.costDate = costDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Date getCostDate() {
        return costDate;
    }

    public void setCostDate(Date costDate) {
        this.costDate = costDate;
    }
}
