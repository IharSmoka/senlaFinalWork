package com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings;

public class SearchInstruction<T> {

    private SearchInstructionType searchInstructionType;
    private T val;

    public SearchInstruction(SearchInstructionType searchInstructionType, T val) {
        this.searchInstructionType = searchInstructionType;
        this.val = val;
    }

    public SearchInstructionType getSearchInstructionType() {
        return searchInstructionType;
    }

    public void setSearchInstructionType(SearchInstructionType searchInstructionType) {
        this.searchInstructionType = searchInstructionType;
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }
}
