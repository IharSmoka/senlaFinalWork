package com.senla.training_2019.smolka.model.dto.update_dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

public class CostDynamicChangeDto {

    @Null(groups = {CreateMode.class})
    @NotNull(groups = {UpdateMode.class})
    private Long id;

    @Null(groups = {UpdateMode.class})
    @NotNull(groups = {CreateMode.class})
    private Long positionId;

    @NotEmpty(groups = {CreateMode.class, UpdateMode.class})
    private String cost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    private Date costDate;

    public CostDynamicChangeDto() {
    }

    public CostDynamicChangeDto(Long id, Long positionId, String cost, Date date) {
        this.id = id;
        this.positionId = positionId;
        this.cost = cost;
        this.costDate = date;
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

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }
}
