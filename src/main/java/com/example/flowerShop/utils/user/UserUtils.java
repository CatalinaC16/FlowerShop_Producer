package com.example.flowerShop.utils.user;

import com.example.flowerShop.dto.user.UserPostDTO;
import com.example.flowerShop.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@NoArgsConstructor
public class UserUtils {

    public boolean validateSignUpMap(UserPostDTO user) {

        if (user.getName() == null || user.getName().trim().isEmpty() || user.getName().split("\\s+").length < 2) {
            return false;
        }

        if (user.getContactNumber() == null || user.getContactNumber().trim().isEmpty() || !user.getContactNumber().matches("\\d{10}")) {
            return false;
        }

        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            return false;
        }

        if (user.getRole() == null) {
            return false;
        }

        if (user.getPassword() == null || user.getPassword().length() < 4) {
            return false;
        }

        if (user.getEmail() == null || !user.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            return false;
        }

        return true;
    }


    public static void updateUserValues(User userExisting, UserPostDTO user) {
        if (Objects.nonNull(user.getName()) && !"".equalsIgnoreCase(user.getName())) {
            userExisting.setName(user.getName());
        }
        if (Objects.nonNull(user.getAddress()) && !"".equalsIgnoreCase(user.getAddress())) {
            userExisting.setAddress(user.getAddress());
        }
        if (Objects.nonNull(user.getContactNumber()) && !"".equalsIgnoreCase(user.getContactNumber())) {
            userExisting.setContactNumber(user.getContactNumber());
        }
        if (Objects.nonNull(user.getEmail()) && !"".equalsIgnoreCase(user.getEmail())) {
            userExisting.setEmail(user.getEmail());
        }
        if (Objects.nonNull(user.getPassword()) && !"".equalsIgnoreCase(user.getPassword())) {
            userExisting.setPassword(user.getPassword());
        }
        if (Objects.nonNull(user.getRole())) {
            userExisting.setRole(user.getRole());
        }
    }
}
