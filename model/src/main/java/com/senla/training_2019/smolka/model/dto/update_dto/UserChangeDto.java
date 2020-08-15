package com.senla.training_2019.smolka.model.dto.update_dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;

import javax.validation.constraints.*;
import java.util.Date;

public class UserChangeDto {

    @Null(groups = {CreateMode.class})
    @NotNull(groups = {UpdateMode.class})
    private Long id;

    @NotEmpty(groups = {CreateMode.class, UpdateMode.class})
    @Size(min = 5, max = 50, groups = {CreateMode.class, UpdateMode.class})
    private String firstName;

    @NotEmpty(groups = {CreateMode.class, UpdateMode.class})
    @Size(min = 5, max = 50, groups = {CreateMode.class, UpdateMode.class})
    private String secondName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(groups = {CreateMode.class, UpdateMode.class})
    private Date birthDate;

    @NotEmpty(groups = {CreateMode.class, UpdateMode.class})
    @Email(groups = {CreateMode.class, UpdateMode.class})
    @Size(min = 5, max = 128, groups = {CreateMode.class, UpdateMode.class})
    private String email;

    @NotEmpty(groups = {CreateMode.class, UpdateMode.class})
    @Size(min = 6, max = 50, groups = {CreateMode.class, UpdateMode.class})
    private String login;

    @NotEmpty(groups = {CreateMode.class, UpdateMode.class})
    @Size(min = 8, max = 50, groups = {CreateMode.class, UpdateMode.class})
    private String password;

    public UserChangeDto() {
    }

    public UserChangeDto(Long id, String firstName, String secondName, Date birthDate, String email, String login, String password) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDate = birthDate;
        this.email = email;
        this.login = login;
        this.password = password;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
