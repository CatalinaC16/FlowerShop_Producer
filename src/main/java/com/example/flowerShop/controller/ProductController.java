package com.example.flowerShop.controller;

import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.entity.Product;
import com.example.flowerShop.service.impl.ProductServiceImpl;
import com.example.flowerShop.utils.category.CategoryName;
import com.example.flowerShop.utils.user.UserRole;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private HttpSession session;

    /**
     * Injected constructor
     *
     * @param productServiceImpl
     */
    @Autowired
    public ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    /**
     * Gets list of products from db
     *
     * @return ModelAndView
     */
    @GetMapping("/listOfProducts")
    public ModelAndView getAllProducts(@RequestParam(name = "category", required = false) String categoryName,
                                       @RequestParam(name = "sortPrice", required = false) String sortPrice,
                                       @RequestParam(name = "search", required = false) String searchQuery) {
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            List<ProductDetailedDTO> products = new ArrayList<>();
            modelAndView = new ModelAndView("listOfProducts");
            if (categoryName == null && sortPrice == null && searchQuery == null) {
                LOGGER.info("Request for list of products");
                products = this.productServiceImpl.getAllProducts().getBody();
            } else {
                LOGGER.info("Request for list of filtered products");
                products = this.productServiceImpl.getAllFilteredProducts(categoryName, sortPrice, searchQuery);
            }
            modelAndView.addObject("products", products);
            modelAndView.addObject("category", this.productServiceImpl.getCategoryName(categoryName));
            modelAndView.addObject("user", currentUser);
        } else {
            modelAndView = new ModelAndView();
            modelAndView.setView(new RedirectView("/login"));
        }
        return modelAndView;
    }

    /**
     * Gets product by id
     *
     * @param id
     * @return ResponseEntity<ProductDetailedDTO>
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDetailedDTO> getProductById(@PathVariable UUID id) {
        LOGGER.info("Request for product with id={}", id);
        return this.productServiceImpl.getProductById(id);
    }

    /**
     * Create product view
     *
     * @return ModelAndView
     */
    @GetMapping("/add")
    public ModelAndView createProduct() {
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            if (currentUser.getRole().equals(UserRole.ADMIN)) {
                modelAndView = new ModelAndView("createProduct");
                modelAndView.addObject("product", new Product());
            } else {
                modelAndView = new ModelAndView("accessDenied");
            }
            modelAndView.addObject("user", currentUser);
        } else {
            modelAndView = new ModelAndView();
            modelAndView.setView(new RedirectView("/login"));
        }
        return modelAndView;
    }

    /**
     * Creates a new product
     *
     * @param productDetailedDTO
     * @return ResponseEntity<String>
     */
    @PostMapping("/createProduct")
    public RedirectView addProduct(@ModelAttribute("product") ProductDetailedDTO productDetailedDTO) {
        LOGGER.info("Request for creating a new product");
        ResponseEntity<String> stringResponseEntity = this.productServiceImpl.addProduct(productDetailedDTO);
        if (stringResponseEntity.getStatusCode() == HttpStatus.CREATED) {
            return new RedirectView("/product/listOfProducts");
        }
        return new RedirectView("/product/add");
    }

    /**
     * Update product view
     *
     * @param id
     * @return ModelAndView
     */
    @GetMapping("/updateProduct/{id}")
    public ModelAndView updateProduct(@PathVariable UUID id) {
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            if (currentUser.getRole().equals(UserRole.ADMIN)) {
                modelAndView = new ModelAndView("updateProduct");
                modelAndView.addObject("product", this.getProductById(id).getBody());
            } else {
                modelAndView = new ModelAndView("accessDenied");
            }
            modelAndView.addObject("user", currentUser);
        } else {
            modelAndView = new ModelAndView();
            modelAndView.setView(new RedirectView("/login"));
        }
        return modelAndView;
    }

    /**
     * Updates an existing product by id
     *
     * @param id
     * @param productDetailedDTO
     * @return RedirectView
     */
    @PostMapping("/update/{id}")
    public RedirectView updateProductById(@PathVariable UUID id, @ModelAttribute("product") ProductDetailedDTO productDetailedDTO) {
        LOGGER.info("Request for updating of a product by id");
        ResponseEntity<String> response = this.productServiceImpl.updateProductById(id, productDetailedDTO);
        if (response.getStatusCode() == HttpStatus.OK) {
            return new RedirectView("/product/listOfProducts");
        }
        return new RedirectView("/product/updateProduct/" + id);
    }

    /**
     * Deletes a product by id
     *
     * @param id
     * @return RedirectView
     */
    @PostMapping("/delete/{id}")
    public RedirectView deleteProductById(@PathVariable UUID id) {
        LOGGER.info("Request for deleting an user by id");
        this.productServiceImpl.deleteProductById(id);
        return new RedirectView("/product/listOfProducts?");
    }
}
