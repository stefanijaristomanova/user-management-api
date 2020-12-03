package com.userManagement.authservice.service;
import com.userManagement.authservice.dto.request.UserRequest;
import com.userManagement.authservice.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    /**
     * Creates new user
     *
     * @param userRequest
     * @return UserResponse
     */
    UserResponse addUser(UserRequest userRequest);

    /**
     * Find all users
     *
     * @return List<UserResponse>
     */
    List<UserResponse> findAllUsers();

    /**
     * Find user by id
     *
     * @param id
     * @return UserResponse
     */
    UserResponse findUserById(Long id);

    /**
     * Edit user by id
     *
     * @param userRequest
     * @param id
     * @return UserResponse
     */
    UserResponse replaceUserById(UserRequest userRequest, Long id);

    /**
     * Remove user by id
     *
     * @param id
     */
    void removeUserById(Long id);
}
