package com.example.flowerShop.service.impl;

import com.example.flowerShop.constants.ReviewConstants;
import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.dto.review.ReviewDTO;
import com.example.flowerShop.dto.review.ReviewDetailedDTO;
import com.example.flowerShop.entity.Product;
import com.example.flowerShop.entity.Review;
import com.example.flowerShop.entity.User;
import com.example.flowerShop.mapper.ProductMapper;
import com.example.flowerShop.mapper.ReviewMapper;
import com.example.flowerShop.repository.ProductRepository;
import com.example.flowerShop.repository.ReviewRepository;
import com.example.flowerShop.repository.UserRepository;
import com.example.flowerShop.service.ReviewService;
import com.example.flowerShop.utils.Utils;
import com.example.flowerShop.utils.review.ReviewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProductMapper productMapper;
    private final ReviewUtils reviewUtils;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);

    /**
     * Injected constructor
     *
     * @param userRepository
     * @param productRepository
     * @param reviewRepository
     * @param reviewMapper
     * @param reviewUtils
     * @param productMapper
     */
    @Autowired
    public ReviewServiceImpl(UserRepository userRepository,
                             ProductRepository productRepository,
                             ReviewRepository reviewRepository,
                             ReviewMapper reviewMapper,
                             ReviewUtils reviewUtils,
                             ProductMapper productMapper) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.reviewUtils = reviewUtils;
        this.productMapper = productMapper;
    }

    /**
     * Retrieves all reviews by product
     *
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<List<ReviewDTO>> getAllReviewsByProductId(UUID id) {
        try {
            LOGGER.info("Retrieves the list of reviews by a product");
            Optional<Product> product = productRepository.findById(id);
            List<Review> reviews = reviewRepository.findAllByProduct(product.get());
            return new ResponseEntity<>(reviewMapper.convertListToDtoWithObjects(reviews), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * Gets list of all reviews
     *
     * @return ResponseEntity<List < ReviewDTO>>
     */
    @Override
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        try {
            LOGGER.info("Retrieves the list of all reviews");
            List<Review> reviews = reviewRepository.findAll();
            return new ResponseEntity<>(reviewMapper.convertListToDtoWithObjects(reviews), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * Gets review by id
     *
     * @param id
     * @return ResponseEntity<ReviewDTO>
     */
    @Override
    public ResponseEntity<ReviewDTO> getReviewById(UUID id) {
        try {
            Optional<Review> reviewOptional = reviewRepository.findById(id);
            if (reviewOptional.isPresent()) {
                LOGGER.info("Gets review by id");
                Review review = reviewOptional.get();
                return new ResponseEntity<>(reviewMapper.convertEntToDtoWithObjects(review), HttpStatus.OK);
            } else {
                LOGGER.error("Review not found");
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a new review
     *
     * @param reviewDetailedDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> addReview(ReviewDetailedDTO reviewDetailedDTO) {
        try {
            if (this.reviewUtils.validateReviewMap(reviewDetailedDTO)) {
                LOGGER.info("Review is creating...");
                Optional<User> user = userRepository.findById(reviewDetailedDTO.getId_user());
                Optional<Product> product = productRepository.findById(reviewDetailedDTO.getId_product());

                if (product.isPresent() && user.isPresent()) {
                    ReviewDTO reviewDTO = reviewMapper.convToReviewWithUserAndProduct(reviewDetailedDTO, user.get(), product.get());
                    reviewRepository.save(reviewMapper.convertToEntity(reviewDTO));
                    return Utils.getResponseEntity(ReviewConstants.REVIEW_CREATED, HttpStatus.CREATED);
                } else {
                    LOGGER.error("Review was not created");
                    return Utils.getResponseEntity(ReviewConstants.SOMETHING_WENT_WRONG_AT_CREATING_REVIEW, HttpStatus.BAD_REQUEST);
                }
            } else {
                return Utils.getResponseEntity(ReviewConstants.INVALID_DATA_AT_CREATING_REVIEW, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(ReviewConstants.SOMETHING_WENT_WRONG_AT_CREATING_REVIEW, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * Updates an existing review by a given id and some review data
     *
     * @param id
     * @param reviewDetailedDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> updateReviewById(UUID id, ReviewDetailedDTO reviewDetailedDTO) {
        try {
            Optional<Review> reviewOptional = reviewRepository.findById(id);
            if (reviewOptional.isPresent()) {
                LOGGER.info("Review is updating...");
                Review reviewExisting = reviewOptional.get();
                reviewUtils.updateReview(reviewExisting, reviewDetailedDTO);
                reviewRepository.save(reviewExisting);
                return Utils.getResponseEntity(ReviewConstants.DATA_MODIFIED, HttpStatus.OK);
            } else {
                LOGGER.error("Review was not updated");
                return Utils.getResponseEntity(ReviewConstants.INVALID_REVIEW, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(ReviewConstants.SOMETHING_WENT_WRONG_AT_UPDATING_REVIEW, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * Delete review by a given id
     *
     * @param id
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> deleteReviewById(UUID id) {
        try {
            Optional<Review> reviewOptional = reviewRepository.findById(id);
            if (reviewOptional.isPresent()) {
                LOGGER.info("Review was deleted");
                reviewRepository.deleteById(id);
                return Utils.getResponseEntity(ReviewConstants.REVIEW_DELETED, HttpStatus.OK);
            } else {
                LOGGER.error("Review was not found");
                return Utils.getResponseEntity(ReviewConstants.INVALID_REVIEW, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(ReviewConstants.SOMETHING_WENT_WRONG_AT_DELETING_REVIEW, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * Gets all products for a certain review
     *
     * @return ResponseEntity<List < ProductDetailedDTO>>
     */
    @Override
    public ResponseEntity<List<ProductDetailedDTO>> getAllProductsForReview() {
        try {
            List<Product> products = productRepository.findAll();
            LOGGER.info("List with all products dor a specific review is retrieved");
            return new ResponseEntity<>(productMapper.convertListToDTO(products), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
