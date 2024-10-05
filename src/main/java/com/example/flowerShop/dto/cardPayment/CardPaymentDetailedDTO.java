package com.example.flowerShop.dto.cardPayment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CardPaymentDetailedDTO {

    private UUID id;
    private String numarCard;
    private Integer cvv;
    private Long totalPrice;
    private LocalDateTime orderDate;
}
