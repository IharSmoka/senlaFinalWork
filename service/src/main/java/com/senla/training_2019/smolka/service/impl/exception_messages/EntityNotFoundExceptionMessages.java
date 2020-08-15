package com.senla.training_2019.smolka.service.impl.exception_messages;

public enum EntityNotFoundExceptionMessages {
    STORE_NOT_EXIST("Store doesnt exists!"),
    PRODUCT_NOT_EXIST("Product doesnt exists!"),
    CATEGORY_NOT_EXIST("Category doesnt exists!"),
    COUNTRY_NOT_EXIST("Country doesnt exists!"),
    CITY_NOT_EXIST("City doesnt exists!"),
    ADDRESS_NOT_EXIST("Address doesnt exists!"),
    MAKER_NOT_EXIST("Maker doesnt exists!"),
    POSITION_NOT_EXIST("Position doesnt exists!"),
    USER_NOT_EXIST("User doesnt exists!"),
    COST_NOT_EXIST("Cost dynamic doesnt exists!");

    private final String msg;

    EntityNotFoundExceptionMessages(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
