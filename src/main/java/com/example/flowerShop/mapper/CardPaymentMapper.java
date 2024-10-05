package com.example.flowerShop.mapper;

import com.example.flowerShop.dto.cardPayment.CardPaymentDTO;
import com.example.flowerShop.dto.cardPayment.CardPaymentDetailedDTO;

import com.example.flowerShop.entity.*;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentMapper implements Mapper<CardPayment, CardPaymentDTO, CardPaymentDetailedDTO> {

    @Override
    public CardPaymentDetailedDTO convertToDTO(CardPayment cardPayment) {

        if (cardPayment != null) {
            return CardPaymentDetailedDTO.builder()
                    .id(cardPayment.getId())
                    .cvv(cardPayment.getCvv())
                    .numarCard(cardPayment.getNumarCard())
                    .orderDate(cardPayment.getOrderDate())
                    .totalPrice(cardPayment.getTotalPrice())
                    .build();
        }
        return null;
    }

    @Override
    public CardPayment convertToEntity(CardPaymentDTO cardPaymentDTO) {

        if (cardPaymentDTO != null) {
            return CardPayment.builder()
                    .id(cardPaymentDTO.getId())
                    .cvv(cardPaymentDTO.getCvv())
                    .numarCard(cardPaymentDTO.getNumarCard())
                    .orderDate(cardPaymentDTO.getOrderDate())
                    .totalPrice(cardPaymentDTO.getTotalPrice())
                    .build();
        }
        return null;
    }

    public CardPaymentDTO convToProdWithCategory(CardPaymentDetailedDTO cardPaymentDetailedDTO) {

        if (cardPaymentDetailedDTO != null) {
            return CardPaymentDTO.builder()
                    .id(cardPaymentDetailedDTO.getId())
                    .cvv(cardPaymentDetailedDTO.getCvv())
                    .numarCard(cardPaymentDetailedDTO.getNumarCard())
                    .orderDate(cardPaymentDetailedDTO.getOrderDate())
                    .totalPrice(cardPaymentDetailedDTO.getTotalPrice())
                    .build();
        }
        return null;
    }
}
