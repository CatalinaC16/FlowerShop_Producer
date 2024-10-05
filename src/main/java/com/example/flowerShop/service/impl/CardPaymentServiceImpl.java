package com.example.flowerShop.service.impl;

import com.example.flowerShop.constants.CardPaymentConstants;
import com.example.flowerShop.dto.cardPayment.CardPaymentDTO;
import com.example.flowerShop.dto.cardPayment.CardPaymentDetailedDTO;
import com.example.flowerShop.entity.CardPayment;

import com.example.flowerShop.mapper.CardPaymentMapper;
import com.example.flowerShop.repository.CardPaymentRepository;
import com.example.flowerShop.service.CardPaymentService;
import com.example.flowerShop.utils.Utils;
import com.example.flowerShop.utils.cardPayment.CardPaymentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardPaymentServiceImpl implements CardPaymentService {

    private final CardPaymentRepository cardPaymentRepository;

    private final CardPaymentMapper cardPaymentMapper;

    private final CardPaymentUtils cardPaymentUtils;

    private static final Logger LOGGER = LoggerFactory.getLogger(CardPaymentServiceImpl.class);

    /**
     * Injected constructor
     *
     * @param cardPaymentRepository
     * @param cardPaymentMapper
     * @param cardPaymentUtils
     */
    @Autowired
    public CardPaymentServiceImpl(CardPaymentRepository cardPaymentRepository, CardPaymentMapper cardPaymentMapper, CardPaymentUtils cardPaymentUtils) {
        this.cardPaymentRepository = cardPaymentRepository;
        this.cardPaymentMapper = cardPaymentMapper;
        this.cardPaymentUtils = cardPaymentUtils;
    }

    /**
     * Retrieves the list of all payments present in the db
     *
     * @return ResponseEntity<List < CardPaymentDetailedDTO>>
     */
    @Override
    public ResponseEntity<List<CardPaymentDetailedDTO>> getAllPayments() {
        try {
            LOGGER.info("Retrieves the list with payments");
            List<CardPayment> cardPayments = cardPaymentRepository.findAll();
            return new ResponseEntity<>(cardPaymentMapper.convertListToDTO(cardPayments), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * Creates a new payment with the data introduced by the user
     *
     * @param cardPaymentDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> addPayment(CardPaymentDTO cardPaymentDTO) {
        try {
            if (this.cardPaymentUtils.validateCategoryMap(cardPaymentDTO)) {
                LOGGER.info("A new payment was created");
                cardPaymentRepository.save(cardPaymentMapper.convertToEntity(cardPaymentDTO));
                return Utils.getResponseEntity(CardPaymentConstants.PAYMENT_CREATED, HttpStatus.CREATED);
            } else {
                LOGGER.error("The payment was not made");
                return Utils.getResponseEntity(CardPaymentConstants.INVALID_DATA_AT_CREATING_PAYMENT, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(CardPaymentConstants.SOMETHING_WENT_WRONG_AT_CREATING_PAYMENT, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * Deletes an existing payment if it finds it in the db
     *
     * @param id
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> deletePayment(UUID id) {
        try {
            Optional<CardPayment> payment = cardPaymentRepository.findById(id);
            if (payment.isPresent()) {
                LOGGER.info("The payment was deleted");
                cardPaymentRepository.deleteById(id);
                return Utils.getResponseEntity(CardPaymentConstants.PAYMENT_DELETED, HttpStatus.OK);
            } else {
                LOGGER.error("The payment was not deleted");
                return Utils.getResponseEntity(CardPaymentConstants.INVALID_PAYMENT, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(CardPaymentConstants.SOMETHING_WENT_WRONG_AT_DELETING_PAYMENT, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
