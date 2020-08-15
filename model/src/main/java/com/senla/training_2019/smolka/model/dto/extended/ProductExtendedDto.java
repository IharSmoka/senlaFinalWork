package com.senla.training_2019.smolka.model.dto.extended;

import com.senla.training_2019.smolka.model.dto.CategoryDto;
import com.senla.training_2019.smolka.model.dto.simple.MakerSimpleDto;

public class ProductExtendedDto {

    private Long id;
    private String productName;
    private MakerExtendedDto maker;
    private CategoryDto category;

    public ProductExtendedDto() {

    }

    public ProductExtendedDto(Long id, String productName, MakerExtendedDto maker, CategoryDto category) {
        this.id = id;
        this.category = category;
        this.maker = maker;
        this.productName = productName;
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

    public MakerExtendedDto getMaker() {
        return maker;
    }

    public void setMaker(MakerExtendedDto maker) {
        this.maker = maker;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }
}
