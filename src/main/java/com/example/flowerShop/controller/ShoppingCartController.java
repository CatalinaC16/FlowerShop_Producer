package com.example.flowerShop.controller;

import com.example.flowerShop.dto.shoppingCart.ShoppingCartDTO;
import com.example.flowerShop.dto.shoppingCart.ShoppingCartDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.service.impl.ShoppingCartServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
public class ShoppingCartController {

    private final ShoppingCartServiceImpl shoppingCartService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);

    @Autowired
    private HttpSession session;

    /**
     * Injected constructor
     *
     * @param shoppingCartService
     */
    @Autowired
    public ShoppingCartController(ShoppingCartServiceImpl shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    /**
     * Returns a list of shopping carts
     *
     * @return ResponseEntity<List < ShoppingCartDTO>>
     */
    @GetMapping("/cart/get/all")
    public ResponseEntity<List<ShoppingCartDTO>> getAllCarts() {
        LOGGER.info("Request for list of shopping carts");
        return this.shoppingCartService.getAllCarts();
    }

    /**
     * Gets cart page with info by current user logged in
     *
     * @return ModelAndView
     */
    @GetMapping("/cart/getByUser")
    public ModelAndView getCartByUserId() {
        LOGGER.info("The cart for current user logged in was retrieved");
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            modelAndView = new ModelAndView("shoppingCart");
            modelAndView.addObject("shoppingCart", this.shoppingCartService.getCartByUserId(currentUser.getId()).getBody());
        } else {
            modelAndView = new ModelAndView();
            modelAndView.setView(new RedirectView("/login"));
        }
        return modelAndView;
    }

    /**
     * After login user redirected to another page where creating of cart is made
     *
     * @return ModelAndView
     */
    @GetMapping("/createCart")
    public ModelAndView createCart() {
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            modelAndView = new ModelAndView("home");
            if (currentUser.getId() != null) {
                modelAndView.addObject("user", currentUser);
            }
        } else {
            modelAndView = new ModelAndView();
            modelAndView.setView(new RedirectView("/login"));
        }
        return modelAndView;
    }

    /**
     * The cart is created if does not exist for current user and redirected to list of products in best case
     *
     * @return RedirectView
     */
    @PostMapping("/cart/add")
    public RedirectView addCart(@RequestBody ShoppingCartDetailedDTO shoppingCartDetailedDTO) {
        LOGGER.info("Shopping cart was created for current user");
        ResponseEntity<String> response = this.shoppingCartService.addCart(shoppingCartDetailedDTO);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return new RedirectView("/product/listOfProducts?");
        }
        return new RedirectView("/userProfile?");
    }

    /**
     * Update of cart by user id  with new order items is performed
     *
     * @return ModelAndView
     */
    @PostMapping("/cart/updateByUserId/{id}")
    public ModelAndView updateCartByUserID(@PathVariable UUID id, @RequestBody ShoppingCartDetailedDTO shoppingCartDetailedDTO) {
        LOGGER.info("Update cart for current user");
        this.shoppingCartService.updateCartByUserID(id, shoppingCartDetailedDTO);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/cart/getByUser"));
        return modelAndView;
    }

    /**
     * Request for  shopping cart delete
     *
     * @param id
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/cart/delete/{id}")
    public ResponseEntity<String> deleteCartByID(@PathVariable UUID id) {
        LOGGER.info("Delete current user's cart by id");
        return this.shoppingCartService.deleteCartById(id);
    }

    /**
     * Update cart by deleting of an order item
     *
     * @param id_user
     * @param orderItemId
     * @return RedirectView
     */
    @PostMapping("/delete/orderItemFromCart/{id_user}")
    public RedirectView deleteOrderItemFromCart(@PathVariable UUID id_user, @RequestParam("orderItemId") UUID orderItemId) {
        LOGGER.info("Update cart by deleting an item");
        ResponseEntity<String> response = this.shoppingCartService.deleteOrderItemFromCart(id_user, orderItemId);
        if (response.getStatusCode() == HttpStatus.OK) {
            return new RedirectView("/product/listOfProducts?");
        }
        return new RedirectView("/cart/getByUser?");
    }
}
