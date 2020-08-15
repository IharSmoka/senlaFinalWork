package com.senla.training_2019.smolka.controllers;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IUserInfoService;
import com.senla.training_2019.smolka.model.dto.MessageDto;
import com.senla.training_2019.smolka.model.dto.update_dto.UserChangeDto;
import com.senla.training_2019.smolka.model.dto.extended.UserInfoExtendedDto;
import com.senla.training_2019.smolka.model.dto.validation_modes.CreateMode;
import com.senla.training_2019.smolka.model.dto.validation_modes.UpdateMode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final static String CREATE_IS_SUCCESS = "User created successfully!";
    private final static String UPDATE_IS_SUCCESS = "User updated successfully!";
    private final static String DELETE_IS_SUCCESS = "User removed successfully!";
    private final static String MODERATOR_IS_CREATED = "User is moderator now!";
    private final IUserInfoService userInfoService;
    private final PasswordEncoder encoder;

    public UserController(IUserInfoService userInfoService, PasswordEncoder encoder) {
        this.userInfoService = userInfoService;
        this.encoder = encoder;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> createUser(@RequestBody @Validated(CreateMode.class) UserChangeDto userInfo) throws InternalServiceException {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfoService.createUser(userInfo);
        return ResponseEntity.ok(new MessageDto(CREATE_IS_SUCCESS));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> updateUser(@RequestBody @Validated(UpdateMode.class) UserChangeDto userInfo) throws EntityNotFoundException, InternalServiceException {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userInfoService.updateUser(userInfo, username);
        return ResponseEntity.ok(new MessageDto(UPDATE_IS_SUCCESS));
    }

    @PutMapping(value = "/moderator")
    public ResponseEntity<MessageDto> setModeratorRoleToUser(@RequestParam(value = "id") Long userId) throws EntityNotFoundException, InternalServiceException {
        userInfoService.setModeratorRoleToUser(userId);
        return ResponseEntity.ok(new MessageDto(MODERATOR_IS_CREATED));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> deleteUser(@RequestParam(value = "id") Long id) throws InternalServiceException, EntityNotFoundException {
        userInfoService.deleteUser(id);
        return ResponseEntity.ok(new MessageDto(DELETE_IS_SUCCESS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoExtendedDto> getUserById(@PathVariable Long id) throws InternalServiceException, EntityNotFoundException {
        return ResponseEntity.ok(userInfoService.findUserById(id));
    }
}
