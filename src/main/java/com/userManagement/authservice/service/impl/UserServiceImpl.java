package com.userManagement.authservice.service.impl;

import com.userManagement.authservice.dto.request.UserRequest;
import com.userManagement.authservice.dto.response.UserResponse;
import com.userManagement.authservice.exception.UserAlreadyExistsException;
import com.userManagement.authservice.exception.UserNotFoundException;
import com.userManagement.authservice.persistence.entity.UserEntity;
import com.userManagement.authservice.persistence.repository.UserRepository;
import com.userManagement.authservice.service.UserService;
 import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
 import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserResponse addUser(UserRequest userRequestBody) {
        if (userRepository.existsByEmail(userRequestBody.getEmail())) {
            throw new UserAlreadyExistsException();
        }


        UserEntity newUser = UserEntity.builder()
                .password(bCryptPasswordEncoder.encode(userRequestBody.getPassword()))
                .email(userRequestBody.getEmail())
                .name(userRequestBody.getName())
                .surname(userRequestBody.getSurname())
                .date(userRequestBody.getDate())
                .phone(userRequestBody.getPhone())
                .date(userRequestBody.getDate())
                .dateCreated(LocalDateTime.now())
                .dateModified(LocalDateTime.now())
                .build();

        UserEntity userEntity = userRepository.save(newUser);
        return convertToUserResponseBody(userEntity);
    }

    @Override
    public List<UserResponse> findAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return convertToUserResponseBodies(userEntities);
    }

    @Override
    public UserResponse findUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return convertToUserResponseBody(userEntity);
    }

    @Override
    public UserResponse replaceUserById(UserRequest userRequestBody, Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        userEntity.setEmail(userRequestBody.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(userRequestBody.getPassword()));
        userEntity.setName(userRequestBody.getName());
        userEntity.setSurname(userRequestBody.getSurname());
        userEntity.setPhone(userRequestBody.getPhone());
        userEntity.setDate(userRequestBody.getDate());
        userEntity.setDateModified(LocalDateTime.now());

        return convertToUserResponseBody(userRepository.save(userEntity));
    }

    @Override
    public void removeUserById(Long id) {
        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    public static UserResponse convertToUserResponseBody(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .surname(userEntity.getSurname())
                .phone(userEntity.getPhone())
                .date(userEntity.getDate())
                .build();
    }

    public static List<UserResponse> convertToUserResponseBodies(List<UserEntity> userEntities) {
        if (userEntities != null) {
            return userEntities.stream()
                    .map(userEntity -> convertToUserResponseBody(userEntity))
                    .collect(Collectors.toList());
        } else return Collections.emptyList();
    }
}
