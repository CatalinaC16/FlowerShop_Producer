package com.example.flowerShop.service.impl;

import com.example.flowerShop.constants.ShoppingCartConstants;
import com.example.flowerShop.constants.UserConstants;
import com.example.flowerShop.dto.shoppingCart.ShoppingCartDTO;
import com.example.flowerShop.dto.shoppingCart.ShoppingCartDetailedDTO;
import com.example.flowerShop.entity.OrderItem;
import com.example.flowerShop.entity.Product;
import com.example.flowerShop.entity.ShoppingCart;
import com.example.flowerShop.entity.User;
import com.example.flowerShop.mapper.ShoppingCartMapper;
import com.example.flowerShop.repository.*;
import com.example.flowerShop.service.ShoppingCartService;
import com.example.flowerShop.utils.Utils;
import com.example.flowerShop.utils.category.CategoryName;
import com.example.flowerShop.utils.shoppingCart.ShoppingCartUtils;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartUtils shoppingCartUtils;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    @Autowired
    private HttpSession session;

    /**
     * Injected constructor
     *
     * @param shoppingCartMapper
     * @param shoppingCartRepository
     * @param shoppingCartUtils
     * @param userRepository
     * @param orderItemRepository
     * @param productRepository
     */
    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartMapper shoppingCartMapper,
                                   ShoppingCartRepository shoppingCartRepository,
                                   ShoppingCartUtils shoppingCartUtils,
                                   UserRepository userRepository,
                                   OrderItemRepository orderItemRepository,
                                   ProductRepository productRepository) {
        this.shoppingCartMapper = shoppingCartMapper;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartUtils = shoppingCartUtils;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    /**
     * Retrieves the list with the existing shopping carts
     *
     * @return ResponseEntity<List < ShoppingCartDTO>>
     */
    @Override
    public ResponseEntity<List<ShoppingCartDTO>> getAllCarts() {
        try {
            LOGGER.info("Retrieves the list of carts");
            List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAll();
            return new ResponseEntity<>(shoppingCartMapper.convertListToDtoWithObjects(shoppingCarts), HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("Something went wrong at retrieving of carts");
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Retrieves cart by user id
     *
     * @param id
     * @return ResponseEntity<ShoppingCartDTO>
     */
    @Override
    public ResponseEntity<ShoppingCartDTO> getCartByUserId(UUID id) {
        try {
            Optional<User> user = userRepository.findById(id);
            Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUser(user.get());
            if (shoppingCart.isPresent()) {
                LOGGER.info("Get cart by user id");
                ShoppingCart cartExisting = shoppingCart.get();
                return new ResponseEntity<>(shoppingCartMapper.convertEntToDtoWithObjects(cartExisting), HttpStatus.OK);
            } else {
                LOGGER.info("Cart for user was not found");
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a new cart
     *
     * @param shoppingCartDetailedDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> addCart(ShoppingCartDetailedDTO shoppingCartDetailedDTO) {
        try {
            if (this.shoppingCartUtils.validateCartMap(shoppingCartDetailedDTO)) {
                LOGGER.info("Cart is creating..");
                Optional<User> user = userRepository.findById(shoppingCartDetailedDTO.getId_user());
                Optional<ShoppingCart> cart = shoppingCartRepository.findByUser(user.get());
                if (cart.isEmpty()) {
                    List<OrderItem> items = orderItemRepository.findProjectedByIdIn(shoppingCartDetailedDTO.getId_orderItems());
                    if (user.isPresent()) {
                        LOGGER.info("Cart exists");
                        ShoppingCartDTO shoppingCartDTO = shoppingCartMapper.convToDtoWithObjects(shoppingCartDetailedDTO, items, user);
                        shoppingCartRepository.save(shoppingCartMapper.convertToEntity(shoppingCartDTO));
                        return Utils.getResponseEntity(ShoppingCartConstants.CART_CREATED, HttpStatus.CREATED);
                    } else {
                        return Utils.getResponseEntity(ShoppingCartConstants.SOMETHING_WENT_WRONG_AT_CREATING_CART, HttpStatus.BAD_REQUEST);
                    }
                }
                return Utils.getResponseEntity(ShoppingCartConstants.CART_EXISTS, HttpStatus.OK);
            } else {
                LOGGER.error("Invalid data at creating the cart");
                return Utils.getResponseEntity(ShoppingCartConstants.INVALID_DATA_AT_CREATING_CART, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return Utils.getResponseEntity(ShoppingCartConstants.SOMETHING_WENT_WRONG_AT_CREATING_CART, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update the cart by user id
     *
     * @param id
     * @param shoppingCartDetailedDTO
     * @return
     */
    @Override
    public ResponseEntity<String> updateCartByUserID(UUID id, ShoppingCartDetailedDTO shoppingCartDetailedDTO) {
        try {
            Optional<User> user = userRepository.findById(id);
            Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUser(user.get());
            List<OrderItem> items = shoppingCart.get().getOrderItems();
            items.addAll(orderItemRepository.findProjectedByIdIn(shoppingCartDetailedDTO.getId_orderItems()));
            if (shoppingCart.isPresent()) {
                ShoppingCart existingCart = shoppingCart.get();
                ShoppingCartUtils.updateCartValues(existingCart, shoppingCartDetailedDTO, items);
                shoppingCartRepository.save(existingCart);
                return Utils.getResponseEntity(ShoppingCartConstants.DATA_MODIFIED, HttpStatus.OK);
            } else {
                return Utils.getResponseEntity(ShoppingCartConstants.INVALID_CART, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(ShoppingCartConstants.SOMETHING_WENT_WRONG_AT_UPDATING_CART, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Update cart by changing the order item
     *
     * @param userId
     * @param orderItemId
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> deleteOrderItemFromCart(UUID userId, UUID orderItemId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUser(user);

                if (shoppingCartOptional.isPresent()) {
                    ShoppingCart shoppingCart = shoppingCartOptional.get();
                    List<OrderItem> items = shoppingCart.getOrderItems();

                    Optional<OrderItem> orderItemOptional = items.stream()
                            .filter(item -> item.getId().equals(orderItemId))
                            .findFirst();

                    if (orderItemOptional.isPresent()) {
                        OrderItem orderItem = orderItemOptional.get();
                        Product product = orderItem.getProduct();
                        if (product.getCategory().getName().equals(CategoryName.valueOf(String.valueOf(CategoryName.CUSTOM_BOUQUETS))))
                            this.increaseQuantitiesOfProducts(product.getId());
                        items.remove(orderItem);
                        shoppingCart.setOrderItems(items);
                        ShoppingCartUtils.updatePrice(shoppingCart, items);
                        product.setStock(product.getStock() + orderItem.getQuantity());
                        productRepository.save(product);
                        shoppingCartRepository.save(shoppingCart);
                        orderItemRepository.deleteById(orderItemId);
                        if (product.getCategory().getName().equals(CategoryName.valueOf(String.valueOf(CategoryName.CUSTOM_BOUQUETS))))
                            productRepository.delete(product);
                        return Utils.getResponseEntity(ShoppingCartConstants.ORDER_ITEM_DELETED_FROM_CART, HttpStatus.OK);
                    } else {
                        return Utils.getResponseEntity(ShoppingCartConstants.ORDER_ITEM_NOT_FOUND_IN_CART, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return Utils.getResponseEntity(ShoppingCartConstants.INVALID_CART, HttpStatus.BAD_REQUEST);
                }
            } else {
                return Utils.getResponseEntity(UserConstants.INVALID_USER, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(ShoppingCartConstants.SOMETHING_WENT_WRONG_AT_UPDATING_CART, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void increaseQuantitiesOfProducts(UUID id) {
        List<Product> products = (List<Product>) this.session.getAttribute("products_" + id);
        List<Integer> quantities = (List<Integer>) this.session.getAttribute("quantity_" + id);
        if (products.size() != quantities.size()) {
            throw new IllegalArgumentException("The size of products and quantities must be the same.");
        }
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int newQuantity = product.getStock() + quantities.get(i);
            product.setStock(newQuantity);
            productRepository.save(product);
        }
    }

    /**
     * Delete cart by id
     *
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<String> deleteCartById(UUID id) {
        try {
            Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(id);
            if (shoppingCart.isPresent()) {
                LOGGER.info("Cart was deleted");
                shoppingCartRepository.deleteById(id);
                return Utils.getResponseEntity(ShoppingCartConstants.CART_DELETED, HttpStatus.OK);
            } else {
                LOGGER.error("Cart was not found");
                return Utils.getResponseEntity(ShoppingCartConstants.INVALID_CART, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(ShoppingCartConstants.SOMETHING_WENT_WRONG_AT_DELETING_CART, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
