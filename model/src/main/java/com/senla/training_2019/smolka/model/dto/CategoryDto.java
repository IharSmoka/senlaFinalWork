package com.senla.training_2019.smolka.model.dto;

import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public class CategoryDto {

    @Null(groups = {CreateMode.class})
    @NotNull(groups = {UpdateMode.class})
    private Integer id;

    @NotEmpty(groups = {CreateMode.class, UpdateMode.class})
    @Size(min = 4, max = 50, groups = {CreateMode.class, UpdateMode.class})
    private String categoryName;

    public CategoryDto() {

    }

    public CategoryDto(Integer id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CategoryDto(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
