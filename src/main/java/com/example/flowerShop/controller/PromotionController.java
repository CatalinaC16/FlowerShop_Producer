package com.example.flowerShop.controller;

import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.dto.promotion.PromotionDTO;
import com.example.flowerShop.dto.promotion.PromotionDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.service.impl.PromotionServiceImpl;
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
@RequestMapping("/promotion")
@CrossOrigin("*")
public class PromotionController {

    private final PromotionServiceImpl promotionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionController.class);

    @Autowired
    private HttpSession session;

    /**
     * Injected constructor
     *
     * @param promotionService
     */
    @Autowired
    public PromotionController(PromotionServiceImpl promotionService) {
        this.promotionService = promotionService;
    }

    /**
     * Retrieves the list of promotions
     *
     * @return ModelAndView
     */
    @GetMapping("/get/all")
    public ModelAndView getAllPromotions() {
        LOGGER.info("Retrieves the list of promotions");
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            if (currentUser.getRole().equals(UserRole.ADMIN)) {
                modelAndView = new ModelAndView("listOfPromotions");
                List<PromotionDTO> promotions = this.promotionService.getAllPromotions().getBody();
                modelAndView.addObject("promotions", promotions);
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
     * Gets promotion by id
     *
     * @param id
     * @return ResponseEntity<PromotionDTO>
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<PromotionDTO> getPromotionById(@PathVariable UUID id) {
        LOGGER.info("Promotion with id {} was retrieved", id);
        return this.promotionService.getPromotionById(id);
    }

    /**
     * Redirects to the create promotion page
     *
     * @return ModelAndView
     */
    @GetMapping("/createPromotion")
    public ModelAndView getAllProductsForPromotion() {
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            if (currentUser.getRole().equals(UserRole.ADMIN)) {
                modelAndView = new ModelAndView("createPromotion");
                List<ProductDetailedDTO> prods = this.promotionService.getAllProductsForPromotion().getBody();
                prods = prods.stream()
                        .filter(product -> product.getStock() > 1)
                        .collect(Collectors.toList());
                modelAndView.addObject("products", prods);
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
     * Creates a new promotion with a set of 1 or more products and a discount
     *
     * @param promotionDetailedDTO
     * @param request
     * @return ModelAndView
     */
    @PostMapping("/add")
    public ModelAndView addPromotion(@RequestBody PromotionDetailedDTO promotionDetailedDTO, HttpServletRequest request) {
        ResponseEntity<String> response = this.promotionService.addPromotion(promotionDetailedDTO);
        ModelAndView modelAndView = new ModelAndView();
        if (response.getStatusCode() == HttpStatus.CREATED) {
            modelAndView.setView(new RedirectView("/promotion/get/all"));
        } else {
            String referer = request.getHeader("Referer");
            modelAndView.setView(new RedirectView(referer));
        }
        return modelAndView;
    }

    /**
     * Update promotion view
     *
     * @param id
     * @return ModelAndView
     */
    @GetMapping("/updatePromotion/{id}")
    public ModelAndView updatePromotion(@PathVariable UUID id) {
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            if (currentUser.getRole().equals(UserRole.ADMIN)) {
                modelAndView = new ModelAndView("updatePromotion");
                modelAndView.addObject("promotion", this.promotionService.getPromotionById(id).getBody());
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
     * Update promotion by id and in success case redirects to list of promotions, else remains on update page
     *
     * @param id
     * @param promotionDetailedDTO
     * @param request
     * @return RedirectView
     */
    @PostMapping("/update/{id}")
    public RedirectView updatePromotionById(@PathVariable UUID id, @ModelAttribute("promotion") PromotionDetailedDTO promotionDetailedDTO, HttpServletRequest request) {
        ResponseEntity<String> response = this.promotionService.updatePromotionById(id, promotionDetailedDTO);
        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.info("Promotion was successfully updated");
            return new RedirectView("/promotion/get/all");
        } else {
            LOGGER.info("Promotion was not updated");
            String referer = request.getHeader("Referer");
            return new RedirectView(referer);
        }
    }

    /**
     * Delete promotion by id
     *
     * @param id
     * @param request
     * @return RedirectView
     */
    @PostMapping("/delete/{id}")
    public RedirectView deletePromotionById(@PathVariable UUID id, HttpServletRequest request) {
        ResponseEntity<String> response = this.promotionService.deletePromotionById(id);
        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.info("Promotion was successfully deleted");
            return new RedirectView("/promotion/get/all");
        } else {
            LOGGER.info("Promotion was not deleted");
            String referer = request.getHeader("Referer");
            return new RedirectView(referer);
        }
    }
}
