package com.userManagement.authservice.controller;

import com.userManagement.authservice.dto.request.UserRequest;
import com.userManagement.authservice.dto.response.UserResponse;
import com.userManagement.authservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/users")
@Api(value = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Sign up new user", response = UserResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully registered"),
            @ApiResponse(code = 201, message = "User successfully created"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @ResponseBody
    @PostMapping(value = "/registration", produces = "application/json")
    public UserResponse addUser(@RequestBody @Valid UserRequest userRequest) {
        return userService.addUser(userRequest);
    }

    @ApiOperation(value = "List all users", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of all users"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @ResponseBody
    @GetMapping(produces = "application/json")
    public List<UserResponse> findAllUsers() {
        return userService.findAllUsers();
    }

    @ApiOperation(value = "Find user by id", response = UserResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user by id"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @ResponseBody
    @GetMapping(value = "/{id}", produces = "application/json")
    public UserResponse findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @ApiOperation(value = "Update user by id", response = UserResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated user by id"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @ResponseBody
    @PutMapping(value = "/{id}", produces = "application/json")
    public UserResponse replaceUserById(@RequestBody @Valid UserRequest userRequest, @PathVariable Long id) {
        return userService.replaceUserById(userRequest, id);
    }

    @ApiOperation(value = "Delete user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted user by id"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @ResponseBody
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public void removeUserById(@PathVariable Long id) {
        userService.removeUserById(id);
    }
}