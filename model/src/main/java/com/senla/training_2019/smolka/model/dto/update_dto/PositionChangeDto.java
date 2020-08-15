package com.senla.training_2019.smolka.model.dto.update_dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

public class PositionChangeDto {

    @Null(groups = {CreateMode.class})
    @NotNull(groups = {UpdateMode.class})
    private Long id;

    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    private Long storeId;

    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    private Long productId;

    @Null(groups = {UpdateMode.class})
    @NotEmpty(groups = {CreateMode.class})
    private String cost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @Null(groups = {UpdateMode.class})
    @NotNull(groups = {CreateMode.class})
    private Date costDate;

    public PositionChangeDto() {
    }

    public PositionChangeDto(Long id, Long storeId, Long productId) {
        this.id = id;
        this.storeId = storeId;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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
