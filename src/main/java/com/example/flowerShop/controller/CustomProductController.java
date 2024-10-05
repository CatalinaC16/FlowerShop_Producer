package com.example.flowerShop.controller;

import com.example.flowerShop.dto.customProduct.CustomProductDTO;
import com.example.flowerShop.dto.customProduct.CustomProductDetailedDTO;
import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.service.impl.CustomProductServiceImpl;
import com.example.flowerShop.utils.user.UserRole;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bouquet")
@CrossOrigin("*")
public class CustomProductController {

    private final CustomProductServiceImpl customProductService;

    @Autowired
    private HttpSession session;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomProductController.class);

    @Autowired
    public CustomProductController(CustomProductServiceImpl customProductService) {
        this.customProductService = customProductService;
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<CustomProductDTO>> getAllCustomProducts() {
        return this.customProductService.getAllCustomProducts();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CustomProductDTO> getCustomProductById(@PathVariable UUID id) {
        return this.customProductService.getCustomProductById(id);
    }

    @GetMapping("/create")
    public ModelAndView getAllProductsForPromotion() {
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            modelAndView = new ModelAndView("createBouquet");
            List<ProductDetailedDTO> prods = this.customProductService.getAllProducts().getBody();
            prods = prods.stream()
                    .filter(product -> product.getStock() > 1)
                    .collect(Collectors.toList());
            modelAndView.addObject("products", prods);
            modelAndView.addObject("user", currentUser);
        } else {
            modelAndView = new ModelAndView();
            modelAndView.setView(new RedirectView("/login"));
        }
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addCustomProduct(@RequestBody CustomProductDetailedDTO productDetailedDTO, HttpServletRequest request) {
        ResponseEntity<String> response = this.customProductService.addCustomProduct(productDetailedDTO);
        ModelAndView modelAndView = new ModelAndView();
        if (response.getStatusCode() == HttpStatus.CREATED) {
            modelAndView.setView(new RedirectView("/cart/getByUser"));
        } else {
            String referer = request.getHeader("Referer");
            modelAndView.setView(new RedirectView(referer));
        }
        return modelAndView;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomProductById(@PathVariable UUID id) {
        return this.customProductService.deleteCustomProductById(id);
    }
}
