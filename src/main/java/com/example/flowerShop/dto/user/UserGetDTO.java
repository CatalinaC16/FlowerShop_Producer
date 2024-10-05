package com.example.flowerShop.dto.user;

import com.example.flowerShop.utils.user.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserGetDTO {

    private UUID id;
    private String name;
    private String contactNumber;
    private String email;
    private String address;
    private UserRole role;
}

