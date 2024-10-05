package com.example.flowerShop.controller;

import com.example.flowerShop.dto.orderItem.OrderItemDTO;
import com.example.flowerShop.dto.orderItem.OrderItemDetailedDTO;
import com.example.flowerShop.service.impl.OrderItemServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orderItem")
@CrossOrigin("*")
public class OrderItemController {

    private final OrderItemServiceImpl orderItemServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemController.class);

    /**
     * Dep injection in constructor by using Autowired annotation
     *
     * @param orderItemServiceImpl
     */
    @Autowired
    public OrderItemController(OrderItemServiceImpl orderItemServiceImpl) {
        this.orderItemServiceImpl = orderItemServiceImpl;
    }

    /**
     * Gets list of all order items
     *
     * @return ResponseEntity<List < OrderItemDTO>>
     */
    @GetMapping("/get/all")
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems() {
        LOGGER.info("Request for getting list of order items");
        return this.orderItemServiceImpl.getAllOrderItems();
    }

    /**
     * Gets order item by id
     *
     * @param id
     * @return ResponseEntity<OrderItemDTO>
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable UUID id) {
        LOGGER.info("Request for getting an order item by id");
        return this.orderItemServiceImpl.getOrderItemById(id);
    }

    /**
     * Creates a new order item
     *
     * @param orderItemDetailedDTO
     * @return ResponseEntity<String>
     */
    @PostMapping("/add")
    public ResponseEntity<String> addOrderItem(@RequestBody OrderItemDetailedDTO orderItemDetailedDTO) {
        LOGGER.info("Request for creating an order item");
        return this.orderItemServiceImpl.addOrderItem(orderItemDetailedDTO);
    }

    /**
     * Updates an existing order item given by id
     *
     * @param id
     * @param orderItemDetailedDTO
     * @return ResponseEntity<String>
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateOrderItemById(@PathVariable UUID id, @RequestBody OrderItemDetailedDTO orderItemDetailedDTO) {
        LOGGER.info("Request for updating an order item by id");
        return this.orderItemServiceImpl.updateOrderItemById(id, orderItemDetailedDTO);
    }

    /**
     * Updates the quantity of order item
     *
     * @param id
     * @param id_cart
     * @param action
     * @return RedirectView
     */
    @PostMapping("/updateQuantityOfOrderItem/{id}")
    public RedirectView updateQuantityOrderItemById(@PathVariable UUID id, @RequestParam("cartId") UUID id_cart, @RequestParam("action") String action) {
        LOGGER.info("Request for updating an order item by id");
        this.orderItemServiceImpl.updateQuantityOrderItemById(id, id_cart, action);
        return new RedirectView("/cart/getByUser");
    }

    /**
     * Deletes an existing item given by id
     *
     * @param id
     * @return ResponseEntity<String>
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrderItemById(@PathVariable UUID id) {
        LOGGER.info("Request for deleting an order item by id");
        return this.orderItemServiceImpl.deleteOrderItemById(id);
    }
}
