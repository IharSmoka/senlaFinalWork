package com.senla.training_2019.smolka.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class MessageDto {

    private String msg;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationDate;

    public MessageDto(String msg) {
        this.msg = msg;
        this.operationDate = new Date();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }
}
