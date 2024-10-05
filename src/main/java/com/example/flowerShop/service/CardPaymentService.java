package com.example.flowerShop.service;

import com.example.flowerShop.dto.cardPayment.CardPaymentDTO;
import com.example.flowerShop.dto.cardPayment.CardPaymentDetailedDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CardPaymentService {

    ResponseEntity<List<CardPaymentDetailedDTO>> getAllPayments();

    ResponseEntity<String> addPayment(CardPaymentDTO cardPaymentDTO);

    ResponseEntity<String> deletePayment(UUID id);
}
