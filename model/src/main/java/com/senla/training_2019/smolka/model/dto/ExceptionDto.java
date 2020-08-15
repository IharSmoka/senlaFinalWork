package com.senla.training_2019.smolka.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ExceptionDto {

    private final String exceptionMessage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private final Date date;

    public ExceptionDto(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
        date = new Date();
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public Date getDate() {
        return date;
    }
}
