package com.example.flowerShop.service;

import com.example.flowerShop.dto.user.LoginDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.dto.user.UserPostDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface UserService {

    ResponseEntity<List<UserGetDTO>> getAllUsers();

    ResponseEntity<UserGetDTO> getUserById(UUID id);

    ResponseEntity<String> addUser(UserPostDTO user);

    ResponseEntity<String> updateUserById(UUID id, UserPostDTO user);

    ResponseEntity<String> deleteUserById(UUID id);

    ResponseEntity<UserGetDTO> getUserByEmailAndPassword(LoginDTO loginDTO);
}
