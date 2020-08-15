package com.senla.training_2019.smolka.model.dto.update_dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Date;

public class ProductChangeDto {

    @Null(groups = {CreateMode.class})
    @NotNull(groups = {UpdateMode.class})
    private Long id;

    @NotEmpty(groups = {CreateMode.class, UpdateMode.class})
    @Size(min = 5, max = 50, groups = {CreateMode.class, UpdateMode.class})
    private String productName;

    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    private Long makerId;

    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    private Integer categoryId;

    public ProductChangeDto() {
    }

    public ProductChangeDto(String productName, Long makerId, Integer categoryId) {
        this.productName = productName;
        this.makerId = makerId;
        this.categoryId = categoryId;
    }

    public ProductChangeDto(Long id, String productName, Long makerId, Integer categoryId) {
        this.id = id;
        this.productName = productName;
        this.makerId = makerId;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getMakerId() {
        return makerId;
    }

    public void setMakerId(Long makerId) {
        this.makerId = makerId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
