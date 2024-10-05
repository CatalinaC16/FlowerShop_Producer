package com.example.flowerShop.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO implements Serializable {

    private UUID id;
    private String name;
    private String email;
    private String bodyAction;
    private String body;
}
