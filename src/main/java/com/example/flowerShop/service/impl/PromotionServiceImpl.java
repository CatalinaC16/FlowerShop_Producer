package com.example.flowerShop.service.impl;

import com.example.flowerShop.constants.PromotionConstants;
import com.example.flowerShop.dto.product.ProductDTO;
import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.dto.promotion.PromotionDTO;
import com.example.flowerShop.dto.promotion.PromotionDetailedDTO;
import com.example.flowerShop.entity.*;
import com.example.flowerShop.mapper.ProductMapper;
import com.example.flowerShop.mapper.PromotionMapper;
import com.example.flowerShop.repository.ProductRepository;
import com.example.flowerShop.repository.PromotionRepository;
import com.example.flowerShop.service.PromotionService;
import com.example.flowerShop.utils.Utils;
import com.example.flowerShop.utils.promotion.PromotionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;
    private final ProductRepository productRepository;
    private final PromotionUtils promotionUtils;
    private final ProductMapper productMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionServiceImpl.class);

    /**
     * Injected constructor
     *
     * @param promotionRepository
     * @param promotionMapper
     * @param productRepository
     * @param promotionUtils
     * @param productMapper
     */
    @Autowired
    public PromotionServiceImpl(PromotionRepository promotionRepository, PromotionMapper promotionMapper, ProductRepository productRepository, PromotionUtils promotionUtils, ProductMapper productMapper) {
        this.promotionMapper = promotionMapper;
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
        this.promotionUtils = promotionUtils;
        this.productMapper = productMapper;
    }

    /**
     * Get all promotions
     *
     * @return ResponseEntity<List < PromotionDTO>>
     */
    @Override
    public ResponseEntity<List<PromotionDTO>> getAllPromotions() {
        try {
            LOGGER.info("Retrieves all promotions");
            List<Promotion> promotions = promotionRepository.findAll();
            return new ResponseEntity<>(promotionMapper.convertListToDtoWithObjects(promotions), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Get all products for a certain promotion
     *
     * @return ResponseEntity<List < ProductDetailedDTO>>
     */
    @Override
    public ResponseEntity<List<ProductDetailedDTO>> getAllProductsForPromotion() {
        try {
            LOGGER.info("Retrieves products for a cetain promotion");
            List<Product> products = productRepository.findAll();
            return new ResponseEntity<>(productMapper.convertListToDTO(products), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Get promotion by id
     *
     * @param id
     * @return ResponseEntity<PromotionDTO>
     */
    @Override
    public ResponseEntity<PromotionDTO> getPromotionById(UUID id) {
        try {
            Optional<Promotion> promotion = promotionRepository.findById(id);
            if (promotion.isPresent()) {
                LOGGER.info("Get promotion with a certain id");
                Promotion promotionExisting = promotion.get();
                return new ResponseEntity<>(promotionMapper.convertEntToDtoWithObjects(promotionExisting), HttpStatus.OK);
            } else {
                LOGGER.error("Promotion was not found");
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Create a new promotion with a set of products and a discount
     *
     * @param promotionDetailedDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> addPromotion(PromotionDetailedDTO promotionDetailedDTO) {
        try {
            if (this.promotionUtils.validatePromotionMap(promotionDetailedDTO)) {
                LOGGER.info("Create promotion...");
                List<Product> products = productRepository.findProjectedByIdIn(promotionDetailedDTO.getId_products());

                if (products.stream().allMatch(Objects::nonNull) && !products.isEmpty()) {
                    PromotionDTO promotionDTO = promotionMapper.convToDtoWithObjects(promotionDetailedDTO, products);

                    double discountPercentage = promotionDTO.getDiscountPercentage() / 100.0;

                    for (Product product : products) {
                        double discountedPrice = Math.floor(product.getPrice() * (1 - discountPercentage));
                        product.setPrice(discountedPrice);
                        productRepository.save(product);
                    }

                    promotionRepository.save(promotionMapper.convertToEntity(promotionDTO));

                    return Utils.getResponseEntity(PromotionConstants.PROMOTION_CREATED, HttpStatus.CREATED);
                } else {
                    return Utils.getResponseEntity(PromotionConstants.SOMETHING_WENT_WRONG_AT_CREATING_PROMOTION, HttpStatus.BAD_REQUEST);
                }
            } else {
                LOGGER.error("Promotion was not created");
                return Utils.getResponseEntity(PromotionConstants.INVALID_DATA_AT_CREATING_PROMOTION, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(PromotionConstants.SOMETHING_WENT_WRONG_AT_CREATING_PROMOTION, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param id
     * @param promotionDetailedDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> updatePromotionById(UUID id, PromotionDetailedDTO promotionDetailedDTO) {
        try {
            Optional<Promotion> promotionOptional = promotionRepository.findById(id);
            List<Product> products = productRepository.findProjectedByIdIn(promotionDetailedDTO.getId_products());
            if (promotionOptional.isPresent()) {
                LOGGER.info("Update promotion...");
                Promotion promotionExisting = promotionOptional.get();
                Double discountBefore = promotionExisting.getDiscountPercentage() / 100.0;
                PromotionUtils.updatePromotion(promotionExisting, promotionDetailedDTO, products);
                List<Product> productsExisting = promotionExisting.getProducts();
                for (Product product : productsExisting) {
                    double discountedPrice = Math.floor(product.getPrice() * (1 + discountBefore) * (1 - (promotionDetailedDTO.getDiscountPercentage() / 100.0)));
                    product.setPrice(discountedPrice);
                    productRepository.save(product);
                }
                promotionRepository.save(promotionExisting);
                return Utils.getResponseEntity(PromotionConstants.DATA_MODIFIED, HttpStatus.OK);
            } else {
                LOGGER.error("Promotion was not updated");
                return Utils.getResponseEntity(PromotionConstants.INVALID_PROMOTION, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(PromotionConstants.SOMETHING_WENT_WRONG_AT_UPDATING_PROMOTION, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Delete promotion by id
     *
     * @param id
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> deletePromotionById(UUID id) {
        try {
            Optional<Promotion> promotionOptional = promotionRepository.findById(id);
            if (promotionOptional.isPresent()) {
                LOGGER.info("Promotion deleted");
                double discountPercentage = promotionOptional.get().getDiscountPercentage() / 100.0;

                for (Product product : promotionOptional.get().getProducts()) {
                    double discountedPrice = Math.ceil(product.getPrice() * (1 + discountPercentage));
                    product.setPrice(discountedPrice + 1);
                    productRepository.save(product);
                }
                promotionRepository.deleteById(id);
                return Utils.getResponseEntity(PromotionConstants.PROMOTION_DELETED, HttpStatus.OK);
            } else {
                LOGGER.error("Promotion was not found");
                return Utils.getResponseEntity(PromotionConstants.INVALID_PROMOTION, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(PromotionConstants.SOMETHING_WENT_WRONG_AT_DELETING_PROMOTION, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
