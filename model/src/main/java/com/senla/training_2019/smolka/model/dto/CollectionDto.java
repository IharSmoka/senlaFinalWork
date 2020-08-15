package com.senla.training_2019.smolka.model.dto;

import java.util.Collection;

public class CollectionDto {

    private Long globalCount;
    private Collection<?> resultCollection;

    public CollectionDto(Long globalCount, Collection<?> resultCollection) {
        this.globalCount = globalCount;
        this.resultCollection = resultCollection;
    }

    public Long getGlobalCount() {
        return globalCount;
    }

    public void setGlobalCount(Long globalCount) {
        this.globalCount = globalCount;
    }

    public Collection<?> getResultCollection() {
        return resultCollection;
    }

    public void setResultCollection(Collection<?> resultCollection) {
        this.resultCollection = resultCollection;
    }
}
