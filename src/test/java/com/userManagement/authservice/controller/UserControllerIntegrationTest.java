package com.userManagement.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userManagement.authservice.dto.request.UserRequest;
import com.userManagement.authservice.persistence.entity.UserEntity;
import com.userManagement.authservice.persistence.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class UserControllerIntegrationTest {

    private static final String URL = "/users/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        cleanDatabase();
    }

    @After
    public void tearDown() {
        cleanDatabase();
    }

    @Test
    public void shouldSuccessfullyAddNewUser() throws Exception {

        mockMvc.perform(post(URL + "registration")
                .content(objectMapper.writeValueAsString(mockNewUserRequestBody()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("username@me.com"));
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldReturnAllUsers() throws Exception {
        userRepository.save(mockUserEntity());
        userRepository.save(mockUserEntity());

        mockMvc.perform(get(URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(2, userRepository.findAll().size());
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldSuccessfullyFindUserById() throws Exception {
        UserEntity mockUser = userRepository.save(mockUserEntity());

        mockMvc.perform(get(URL + mockUser.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(mockUser.getEmail()));
    }


    @Test
    @WithMockUser(username = "user")
    public void shouldSuccessfullyReplaceUser() throws Exception {
        UserEntity mockUser = userRepository.save(mockUserEntity());
        UserRequest mockUserRequestBody = mockUserRequestBody();
        mockUserRequestBody.setEmail("newName@me.com");

        mockMvc.perform(put(URL + mockUser.getId())
                .content(objectMapper.writeValueAsString(mockUserRequestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(mockUserRequestBody.getEmail()));
    }

    @Test
    @WithMockUser(username = "admin")
    public void shouldSuccessfullyDeleteUser() throws Exception {
        UserEntity mockUser = userRepository.save(mockUserEntity());

        mockMvc.perform(delete(URL + mockUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(0, userRepository.findAll().size());
    }

    private UserEntity mockUserEntity() {
        return UserEntity.builder()
                .name("name")
                .surname("surname")
                .email("username@me.com")
                .password("password")
                .date(LocalDateTime.now().toString())
                .phone("071 392 202")
                .build();
    }

    private UserRequest mockUserRequestBody() {
        return UserRequest.builder()
                .name("name")
                .surname("surname")
                .email("username@me.com")
                .password("password")
                .date(LocalDateTime.now().toString())
                .phone("071 392 202")
                .build();
    }

    private UserRequest mockNewUserRequestBody() {
        return UserRequest.builder()
                .name("name")
                .surname("surname")
                .email("username@me.com")
                .password("password")
                .date(LocalDateTime.now().toString())
                .phone("071 392 202")
                .build();
    }

    private void cleanDatabase() {
        userRepository.deleteAll();
    }
}
