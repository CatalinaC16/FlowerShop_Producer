package com.example.flowerShop.repository;

import com.example.flowerShop.entity.CardPayment;;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardPaymentRepository extends JpaRepository<CardPayment, UUID> {
}
