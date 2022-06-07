package com.aornelas.usersapp.controller;

import com.aornelas.usersapp.domain.User;
import com.aornelas.usersapp.dto.UserDto;
import com.aornelas.usersapp.mapper.MapStructMapper;
import com.aornelas.usersapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {
    private final UserService userService;
    private final MapStructMapper mapStructMapper;

    @Autowired
    public UserController(UserService userService, MapStructMapper mapStructMapper) {
        this.userService = userService;
        this.mapStructMapper = mapStructMapper;
    }

    @GetMapping
    public ResponseEntity<UserDto> getUsers() {
        List<User> users = userService.getUser();

        List<UserDto> usersDto = users
            .stream()
            .map(mapStructMapper::userToUserDto)
            .collect(Collectors.toList());

        return new ResponseEntity(usersDto, HttpStatus.OK);
    }
}