package com.senla.training_2019.smolka.model.dto.extended;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.training_2019.smolka.model.dto.LoginCredentialDto;

import java.util.Date;

public class UserInfoExtendedDto {

    private Long id;
    private String firstName;
    private String secondName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String email;
    private LoginCredentialDto credential;

    public UserInfoExtendedDto() {

    }

    public UserInfoExtendedDto(Long id, String firstName, String secondName, Date birthDate, String email, LoginCredentialDto credential) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDate = birthDate;
        this.email = email;
        this.credential = credential;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LoginCredentialDto getCredential() {
        return credential;
    }

    public void setCredential(LoginCredentialDto credential) {
        this.credential = credential;
    }
}
