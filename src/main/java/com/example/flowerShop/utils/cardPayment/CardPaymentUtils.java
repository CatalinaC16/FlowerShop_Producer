package com.example.flowerShop.utils.cardPayment;

import com.example.flowerShop.dto.cardPayment.CardPaymentDTO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class CardPaymentUtils {

    public boolean validateCategoryMap(CardPaymentDTO cardPaymentDTO) {
        if (cardPaymentDTO.getNumarCard() == null || cardPaymentDTO.getNumarCard().isEmpty()) {
            return false;
        }
        if (cardPaymentDTO.getCvv() == null || cardPaymentDTO.getCvv() <= 99 || cardPaymentDTO.getCvv() >= 1000) {
            return false;
        }
        if (cardPaymentDTO.getTotalPrice() <= 1) {
            return false;
        }
        return true;
    }
}
