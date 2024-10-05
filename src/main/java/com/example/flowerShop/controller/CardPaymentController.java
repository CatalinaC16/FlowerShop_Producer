package com.example.flowerShop.controller;

import com.example.flowerShop.dto.cardPayment.CardPaymentDTO;
import com.example.flowerShop.dto.cardPayment.CardPaymentDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.service.impl.CardPaymentServiceImpl;

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

@RestController
@RequestMapping("/card")
@CrossOrigin("*")
public class CardPaymentController {

    private final CardPaymentServiceImpl cardPaymentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CardPaymentController.class);

    @Autowired
    private HttpSession session;

    /**
     * Injected constructor
     *
     * @param cardPaymentService
     */
    @Autowired
    public CardPaymentController(CardPaymentServiceImpl cardPaymentService) {
        this.cardPaymentService = cardPaymentService;
    }

    /**
     * Gets list of payments
     *
     * @return ModelAndView
     */
    @GetMapping("/listOfPayments")
    public ModelAndView getAllPayments() {
        LOGGER.info("Retrieves list of payments");
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            if (currentUser.getRole().equals(UserRole.ADMIN)) {
                modelAndView = new ModelAndView("listOfPayments");
                List<CardPaymentDetailedDTO> payments = this.cardPaymentService.getAllPayments().getBody();
                modelAndView.addObject("payments", payments);
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
     * Create a new payment view
     *
     * @param price
     * @return ModelAndView
     */
    @GetMapping("/createPayment/{price}")
    public ModelAndView getAllPaymentsOnPage(@PathVariable(name = "price") Long price) {
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            modelAndView = new ModelAndView("createPay");
            modelAndView.addObject("price", price);
        } else {
            modelAndView = new ModelAndView();
            modelAndView.setView(new RedirectView("/login"));
        }
        return modelAndView;
    }

    /**
     * Creates a new payment in the db
     *
     * @param cardPaymentDTO
     * @param request
     * @return ModelAndView
     */
    @PostMapping("/add")
    public ModelAndView addPayment(@ModelAttribute CardPaymentDTO cardPaymentDTO, HttpServletRequest request) {
        LOGGER.info("Creates a new payment");
        ResponseEntity<String> response = this.cardPaymentService.addPayment(cardPaymentDTO);
        ModelAndView modelAndView = new ModelAndView();
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        if (response.getStatusCode() == HttpStatus.CREATED) {
            modelAndView.setView(new RedirectView("/order/getByUser/all/" + currentUser.getId()));
        } else {
            String referer = request.getHeader("Referer");
            modelAndView.setView(new RedirectView(referer));
        }
        return modelAndView;
    }

    /**
     * Deletes a payment that was made
     *
     * @param id
     * @return RedirectView
     */
    @PostMapping("/delete/{id}")
    public RedirectView deletePayment(@PathVariable UUID id) {
        this.cardPaymentService.deletePayment(id);
        return new RedirectView("/card/listOfPayments");
    }
}
