package com.joycart.backend.controller;

import com.joycart.backend.model.User;
import com.joycart.backend.dto.LoginRequestDTO;
import com.joycart.backend.dto.LoginResponseDTO;
import com.joycart.backend.dto.ErrorResponseDTO;
import com.joycart.backend.service.UserService;
import com.joycart.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

     @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        logger.info("Received registration request for user: {}, phone: {}, email: {}", 
                   user.getUsername(), user.getPhoneNumber(), user.getEmail());

        // Check if phone number already exists
        if (userService.existsByPhoneNumber(user.getPhoneNumber())) {
            logger.warn("Registration failed - Phone number already exists: {}",
                    user.getPhoneNumber());
            return ResponseEntity.badRequest().body("Phone number already exists!");
        }

        if (user.getEmail() != null && !user.getEmail().isEmpty()
                && userService.existsByEmail(user.getEmail())) {
            logger.warn("Registration failed - Email already exists: {}", user.getEmail());
            return ResponseEntity.badRequest().body("Email already exists!");
        }
        
        try {
            User savedUser = userService.saveUser(user);
            logger.info("User registered successfully with ID: {} for username: {}", 
                       savedUser.getId(), savedUser.getUsername());
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            logger.error("Error registering user: {} - {}",
                    user.getUsername(), e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error saving user: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        logger.info("Received login request for phone: {}", loginRequestDTO.getPhoneNumber());

        // 入参参数校验
        if (loginRequestDTO.getPhoneNumber() == null || loginRequestDTO.getPhoneNumber().trim().isEmpty()) {
            logger.warn("Login failed - Phone number is empty");
            ErrorResponseDTO errorResponse = new ErrorResponseDTO("error", "Phone number cannot be empty", 400);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (loginRequestDTO.getPassword() == null || loginRequestDTO.getPassword().trim().isEmpty()) {
            logger.warn("Login failed - Password is empty for phone: {}", loginRequestDTO.getPhoneNumber());
            ErrorResponseDTO errorResponse = new ErrorResponseDTO("error", "Password cannot be empty", 400);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            Optional<User> userOptional = userService.getUserByPhoneNumber(loginRequestDTO.getPhoneNumber());
            
            if (!userOptional.isPresent()) {
                logger.warn("Login failed - User not found for phone: {}", loginRequestDTO.getPhoneNumber());
                ErrorResponseDTO errorResponse = new ErrorResponseDTO("error", "Phone number not registered", 400);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            User user = userOptional.get();

            if (!user.getPassword().equals(loginRequestDTO.getPassword())) {
                logger.warn("Login failed - Invalid password for phone: {}", loginRequestDTO.getPhoneNumber());
                ErrorResponseDTO errorResponse = new ErrorResponseDTO("error", "Incorrect password", 400);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getId(), user.getPhoneNumber());
            
            // create return responseDTO
            LoginResponseDTO.UserData userData = new LoginResponseDTO.UserData(
                user.getId(), 
                token
            );
            LoginResponseDTO response = new LoginResponseDTO("success", userData);
            
            logger.info("User logged in successfully: {}", user.getUsername());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error during login for phone: {} - {}", 
                    loginRequestDTO.getPhoneNumber(), e.getMessage(), e);
            ErrorResponseDTO errorResponse = new ErrorResponseDTO("error", "Login service temporarily unavailable", 500);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

     @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

     @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }

     @GetMapping("/phone/{phoneNumber}")
    public Optional<User> getUserByPhoneNumber(@PathVariable String phoneNumber){
        return userService.getUserByPhoneNumber(phoneNumber);
    }

     @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user){
        return userService.updateUser(id, user);
    }

     @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id){
        userService.deleteUser(id);
    }
}