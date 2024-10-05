package com.example.flowerShop.controller;

import com.example.flowerShop.dto.user.LoginDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.dto.user.UserPostDTO;
import com.example.flowerShop.entity.User;
import com.example.flowerShop.service.impl.UserServiceImpl;
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
@RequestMapping()
@CrossOrigin("*")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private HttpSession session;

    /**
     * Injected constructor
     *
     * @param userServiceImpl
     */
    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * Retrieves list of users for admin only for listOfUsers page.
     *
     * @return ModelAndView
     */
    @GetMapping("/listOfUsers")
    public ModelAndView getAllUsers() {
        LOGGER.info("Request for list of users");
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            if (currentUser.getRole().equals(UserRole.ADMIN)) {
                modelAndView = new ModelAndView("listOfUsers");
                List<UserGetDTO> users = this.userServiceImpl.getAllUsers().getBody();
                modelAndView.addObject("users", users);
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
     * Gets the page for creating a new account (sign up)
     *
     * @return ModelAndView
     */
    @GetMapping("/signUp")
    public ModelAndView createUser() {
        ModelAndView modelAndView = new ModelAndView("signUp");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    /**
     * Creates a new user in the db and redirects if success to login page, else back to signUp
     *
     * @param user
     * @return ModelAndView
     */
    @PostMapping("/createUser")
    public ModelAndView addUser(@ModelAttribute("user") UserPostDTO user) {
        LOGGER.info("Request for creating a new user");
        session.invalidate();
        ResponseEntity<String> stringResponseEntity = this.userServiceImpl.addUser(user);
        ModelAndView modelAndView = new ModelAndView();
        if (stringResponseEntity.getStatusCode() == HttpStatus.CREATED) {
            modelAndView.setView(new RedirectView("/login"));
        } else
            modelAndView.setView(new RedirectView("/signUp"));
        return modelAndView;
    }

    /**
     * Gets user by id
     *
     * @param id
     * @return ResponseEntity<UserGetDTO>
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable UUID id) {
        LOGGER.info("Request for a user by id");
        return this.userServiceImpl.getUserById(id);
    }

    /**
     * Gets user profile page for self update or delete of the profile
     *
     * @return ModelAndView
     */
    @GetMapping("/userProfile")
    public ModelAndView userProfile() {
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            LOGGER.info("Gets current logged user profile data");
            modelAndView = new ModelAndView("userProfile");
            modelAndView.addObject("user", currentUser);
        } else {
            modelAndView = new ModelAndView();
            modelAndView.setView(new RedirectView("/login"));
        }
        return modelAndView;
    }

    /**
     * Gets the login page
     *
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginDTO", new LoginDTO());
        return modelAndView;
    }

    /**
     * Logs the user in and redirects in positive case to list of products, else back to login
     *
     * @param loginDTO
     * @return ModelAndView
     */
    @PostMapping("/login/user")
    public ModelAndView loginUser(@ModelAttribute("loginDTO") LoginDTO loginDTO) {
        session.invalidate();
        LOGGER.info("Invalidate session if exists and log in the new user");
        ResponseEntity<UserGetDTO> response = this.userServiceImpl.getUserByEmailAndPassword(loginDTO);
        ModelAndView modelAndView = new ModelAndView();
        if (response.getStatusCode() == HttpStatus.OK) {
            session.setAttribute("loggedInUser", response.getBody());
            modelAndView.setView(new RedirectView("/createCart"));
        } else
            modelAndView.setView(new RedirectView("/login"));
        return modelAndView;
    }

    /**
     * Logs the user out of the app and redirects to login page
     *
     * @return RedirectView
     */
    @GetMapping("/logout")
    public RedirectView logout() {
        LOGGER.info("Request for logging out");
        session.invalidate();
        return new RedirectView("/login");
    }

    /**
     * Gets page for update of a user by id
     *
     * @return ModelAndView
     */
    @GetMapping("/updateUser/{id}")
    public ModelAndView updateUser(@PathVariable UUID id) {
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            modelAndView = new ModelAndView("updateUser");
            User user = this.userServiceImpl.convertToModelObject(id);
            modelAndView.addObject("user", user);
        } else {
            modelAndView = new ModelAndView();
            modelAndView.setView(new RedirectView("/login"));
        }
        return modelAndView;
    }

    /**
     * Updates user by given id and request body, when succes redirects to user profile in case of CUSTOMER
     * and to list of users in case of ADMIN
     *
     * @param id
     * @param user
     * @return ModelAndView
     */
    @PostMapping("/update/{id}")
    public ModelAndView updateUserById(@PathVariable UUID id, @ModelAttribute("user") @RequestBody UserPostDTO user) {
        LOGGER.info("Request for updating data for a user by id");
        ResponseEntity<String> response = this.userServiceImpl.updateUserById(id, user);
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView = new ModelAndView();
        if (response.getStatusCode() == HttpStatus.OK) {
            if (currentUser.getRole().equals(UserRole.ADMIN)) {
                modelAndView.setView(new RedirectView("/listOfUsers"));
                return modelAndView;
            }
            session.setAttribute("loggedInUser", getUserById(user.getId()).getBody());
            modelAndView.setView(new RedirectView("/userProfile"));
        } else
            modelAndView.setView(new RedirectView("/updateUser/" + id));
        return modelAndView;
    }

    /**
     * Deletes user by given id and redirects to login if the current user is CUSTOMER,
     * else redirects to list of users.
     *
     * @param id
     * @return ModelAndView
     */
    @PostMapping("/delete/{id}")
    public ModelAndView deleteUserById(@PathVariable UUID id, HttpServletRequest request) {
        LOGGER.info("Request for deleting a user by id");
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ResponseEntity<String> response = this.userServiceImpl.deleteUserById(id);
        ModelAndView modelAndView = new ModelAndView();
        if (response.getStatusCode() == HttpStatus.OK) {
            if (currentUser.getRole().equals(UserRole.CUSTOMER)) {
                session.invalidate();
                modelAndView.setView(new RedirectView("/login"));
            } else
                modelAndView.setView(new RedirectView("/listOfUsers"));
        } else {
            String referer = request.getHeader("Referer");
            modelAndView.setView(new RedirectView(referer));
        }
        return modelAndView;
    }
}
