package com.example.flowerShop.service.impl;

import com.example.flowerShop.constants.OrderItemConstants;
import com.example.flowerShop.dto.orderItem.OrderItemDTO;
import com.example.flowerShop.dto.orderItem.OrderItemDetailedDTO;
import com.example.flowerShop.entity.OrderItem;
import com.example.flowerShop.entity.Product;
import com.example.flowerShop.entity.ShoppingCart;
import com.example.flowerShop.mapper.OrderItemMapper;
import com.example.flowerShop.repository.OrderItemRepository;
import com.example.flowerShop.repository.ProductRepository;
import com.example.flowerShop.repository.ShoppingCartRepository;
import com.example.flowerShop.service.OrderItemService;
import com.example.flowerShop.utils.Utils;
import com.example.flowerShop.utils.order.OrderItemUtils;
import com.example.flowerShop.utils.shoppingCart.ShoppingCartUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    private final ProductRepository productRepository;

    private final ShoppingCartRepository shoppingCartRepository;

    private final OrderItemUtils orderItemUtils;

    private final OrderItemMapper orderItemMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    /**
     * Constructor for inversion of control, used Autowired annotation for Spring to know to do automated injection
     *
     * @param orderItemRepository
     * @param productRepository
     * @param orderItemUtils
     * @param orderItemMapper
     */
    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ProductRepository productRepository,
                                OrderItemUtils orderItemUtils, OrderItemMapper orderItemMapper,
                                ShoppingCartRepository shoppingCartRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.orderItemUtils = orderItemUtils;
        this.orderItemMapper = orderItemMapper;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    /**
     * Returns the entries from the DB for the orderItems table
     *
     * @return ResponseEntity<List < OrderItemDTO>>
     */
    @Override
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems() {

        LOGGER.info("Fetching order items list...");
        try {
            List<OrderItem> orderItems = orderItemRepository.findAll();
            LOGGER.info("Fetching completed, list of order items retrieved");
            return new ResponseEntity<>(orderItemMapper.convertListToDtoWithObjects(orderItems), HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("Error while performing the fetching of the list with order items", exception);
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gets the order item with the given id
     *
     * @param id
     * @return ResponseEntity<OrderItemDTO>
     */
    @Override
    public ResponseEntity<OrderItemDTO> getOrderItemById(UUID id) {

        LOGGER.info("Fetching order item with id = " + id);
        try {
            Optional<OrderItem> orderItem = orderItemRepository.findById(id);
            if (orderItem.isPresent()) {
                OrderItem orderItemExisting = orderItem.get();
                LOGGER.info("Fetching completed, order item retrieved");
                return new ResponseEntity<>(orderItemMapper.convertEntToDtoWithObjects(orderItemExisting), HttpStatus.OK);
            } else {
                LOGGER.error("Order item with id = {} not found in the db", id);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            LOGGER.error("Error while retrieving the order item by id");
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a new order item, if there is a product and the same quantity we cannot create another one
     *
     * @param orderItemDetailedDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> addOrderItem(OrderItemDetailedDTO orderItemDetailedDTO) {

        LOGGER.info("Creating a new order item...");
        try {
            if (this.orderItemUtils.validateOrderItemMap(orderItemDetailedDTO)) {
                Optional<Product> product = productRepository.findById(orderItemDetailedDTO.getId_product());
                product.get().setStock(product.get().getStock() - orderItemDetailedDTO.getQuantity());
                productRepository.save(product.get());
                LOGGER.info("Order item created");
                OrderItemDTO orderItemDTO = orderItemMapper.convToDtoWithObjects(orderItemDetailedDTO, product);
                OrderItem savedOrderItem = orderItemRepository.save(orderItemMapper.convertToEntity(orderItemDTO));
                return Utils.getResponseEntity(String.valueOf(savedOrderItem.getId()), HttpStatus.CREATED);
            } else {
                LOGGER.error("Invalid data was sent for creating the order item");
                return Utils.getResponseEntity(OrderItemConstants.INVALID_DATA_AT_CREATING_ORDER_ITEM, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("Something went wrong at creating the order item");
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(OrderItemConstants.SOMETHING_WENT_WRONG_AT_CREATING_ORDER_ITEM, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Updates the order item by id, on here we can change the product and its quantity on a order item given by id
     *
     * @param id
     * @param orderItemDetailedDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> updateOrderItemById(UUID id, OrderItemDetailedDTO orderItemDetailedDTO) {

        LOGGER.info("Updating the data for an order item with id {}...", id);
        try {
            Optional<OrderItem> orderItemOptional = orderItemRepository.findById(id);
            if (orderItemOptional.isPresent()) {
                OrderItem orderItemExisting = orderItemOptional.get();
                Optional<Product> product;
                if (orderItemDetailedDTO.getId_product() == null) {
                    product = Optional.ofNullable(orderItemExisting.getProduct());
                } else {
                    product = productRepository.findById(orderItemDetailedDTO.getId_product());
                }

                if (orderItemDetailedDTO.getQuantity() == 0) {
                    orderItemDetailedDTO.setQuantity(orderItemExisting.getQuantity());
                }
                OrderItemDTO orderItem = orderItemMapper.convToDtoWithObjects(orderItemDetailedDTO, product);
                OrderItemUtils.updateOrderItemsValues(orderItemExisting, orderItem);
                LOGGER.info("Completed order item update");
                orderItemRepository.save(orderItemExisting);
                return Utils.getResponseEntity(OrderItemConstants.DATA_MODIFIED, HttpStatus.OK);
            } else {
                LOGGER.error("Order Item with id = {} not found in the db", id);
                return Utils.getResponseEntity(OrderItemConstants.INVALID_ORDER_ITEM, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("Something went wrong at updating the order item");
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(OrderItemConstants.SOMETHING_WENT_WRONG_AT_UPDATING_ORDER_ITEM, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Deletes order item by id if it exists in the db
     *
     * @param id
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> deleteOrderItemById(UUID id) {

        LOGGER.info("Deleting the order item with id {}...", id);
        try {
            Optional<OrderItem> orderItemOptional = orderItemRepository.findById(id);
            if (orderItemOptional.isPresent()) {
                OrderItem orderItemExisting = orderItemOptional.get();
                Product product = orderItemExisting.getProduct();
                product.setStock(product.getStock() + orderItemExisting.getQuantity());
                productRepository.save(product);
                orderItemRepository.deleteById(id);
                LOGGER.info("Order item deleted successfully");
                return Utils.getResponseEntity(OrderItemConstants.ORDER_ITEM_DELETED, HttpStatus.OK);
            } else {
                LOGGER.error("Order item with id = {} not found in the db", id);
                return Utils.getResponseEntity(OrderItemConstants.INVALID_ORDER_ITEM, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.error("Something went wrong at deleting the order item");
            e.printStackTrace();
        }
        return Utils.getResponseEntity(OrderItemConstants.SOMETHING_WENT_WRONG_AT_DELETING_ORDER_ITEM, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Updates quantity of order item by id
     *
     * @param id
     * @param id_cart
     * @param action
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> updateQuantityOrderItemById(UUID id, UUID id_cart, String action) {
        try {
            LOGGER.info("The orderItem was updated");
            Optional<OrderItem> orderItemOptional = orderItemRepository.findById(id);
            if (orderItemOptional.isPresent()) {
                OrderItem orderItemExisting = orderItemOptional.get();
                Product product = orderItemExisting.getProduct();
                switch (action) {
                    case "increase":
                        if ((product.getStock() - 1) >= 0) {
                            orderItemExisting.setQuantity(orderItemExisting.getQuantity() + 1);
                            product.setStock(product.getStock() - 1);
                        }
                        break;
                    case "decrease":
                        int newQuantity = orderItemExisting.getQuantity() - 1;
                        product.setStock(product.getStock() + 1);
                        if (newQuantity >= 1) {
                            orderItemExisting.setQuantity(newQuantity);
                        } else {
                            LOGGER.error(OrderItemConstants.INVALID_QUANTITY);
                            return Utils.getResponseEntity(OrderItemConstants.INVALID_QUANTITY, HttpStatus.BAD_REQUEST);
                        }
                        break;
                    default:
                        return Utils.getResponseEntity(OrderItemConstants.INVALID_ACTION, HttpStatus.BAD_REQUEST);
                }
                ShoppingCart shoppingCart = shoppingCartRepository.findById(id_cart).get();
                ShoppingCartUtils.updatePrice(shoppingCart, shoppingCart.getOrderItems());
                shoppingCartRepository.save(shoppingCart);
                productRepository.save(product);
                orderItemRepository.save(orderItemExisting);
                return Utils.getResponseEntity(OrderItemConstants.DATA_MODIFIED, HttpStatus.OK);
            } else {
                LOGGER.error(OrderItemConstants.INVALID_ORDER_ITEM);
                return Utils.getResponseEntity(OrderItemConstants.INVALID_ORDER_ITEM, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("Something went wrong at updating the order item");
            exception.printStackTrace();
            return Utils.getResponseEntity(OrderItemConstants.SOMETHING_WENT_WRONG_AT_UPDATING_ORDER_ITEM, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
