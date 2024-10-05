package com.example.flowerShop.mapper;

import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.dto.user.UserPostDTO;
import com.example.flowerShop.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserPostDTO, UserGetDTO> {

    @Override
    public UserGetDTO convertToDTO(User user) {

        if (user != null) {
            return UserGetDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .contactNumber(user.getContactNumber())
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .role(user.getRole())
                    .build();
        }
        return null;
    }

    @Override
    public User convertToEntity(UserPostDTO userPostDTO) {

        if (userPostDTO != null) {
            return User.builder()
                    .id(userPostDTO.getId())
                    .name(userPostDTO.getName())
                    .contactNumber(userPostDTO.getContactNumber())
                    .email(userPostDTO.getEmail())
                    .password(userPostDTO.getPassword())
                    .address(userPostDTO.getAddress())
                    .role(userPostDTO.getRole())
                    .build();
        }
        return null;
    }
}
