package com.joycart.backend.controller;

import com.joycart.backend.constants.ApiConstants;
import com.joycart.backend.dto.LoginRequestDTO;
import com.joycart.backend.dto.LoginResponseDTO;
import com.joycart.backend.dto.ResponseDTO;
import com.joycart.backend.model.User;
import com.joycart.backend.service.TranslationService;
import com.joycart.backend.service.UserService;
import com.joycart.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.joycart.backend.constants.ApiConstants.TRANSLATIONS_TYPE_SYSTEM_MESSAGE;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private TranslationService translationService;

     @PostMapping("/register")
    public ResponseEntity<ResponseDTO<User>> registerUser(@RequestBody User user){
        logger.info("Received registration request for user: {}, phone: {}, email: {}", 
                   user.getUsername(), user.getPhoneNumber(), user.getEmail());

        // Check if phone number already exists
        if (userService.existsByPhoneNumber(user.getPhoneNumber())) {
            logger.warn("Registration failed - Phone number already exists: {}",
                    user.getPhoneNumber());
            ResponseDTO<User> errorResponse = ResponseDTO.error(ApiConstants.USER_PHONE_EXISTS_MESSAGE);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (user.getEmail() != null && !user.getEmail().isEmpty()
                && userService.existsByEmail(user.getEmail())) {
            logger.warn("Registration failed - Email already exists: {}", user.getEmail());
            ResponseDTO<User> errorResponse = ResponseDTO.error(ApiConstants.USER_EMAIL_EXISTS_MESSAGE);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        try {
            User savedUser = userService.saveUser(user);
            logger.info("User registered successfully with ID: {} for username: {}", 
                       savedUser.getId(), savedUser.getUsername());
            
            // 从数据库查询用户语言偏好对应的成功消息
            String languageCode = savedUser.getLanguagePreference();
            String successMessage = translationService.getTranslation(
                    ApiConstants.TRANSLATIONS_TYPE_SYSTEM_MESSAGE,
                    0L,
                    ApiConstants.TRANSLATIONS_KEY_REGISTER_SUCCESS,
                    languageCode
            );
            
            if (successMessage == null) {
                // 如果没有找到翻译，回退到英文或使用默认值
                successMessage = translationService.getTranslation(
                        ApiConstants.TRANSLATIONS_TYPE_SYSTEM_MESSAGE,
                        0L,
                        ApiConstants.TRANSLATIONS_KEY_REGISTER_SUCCESS,
                        ApiConstants.LANGUAGE_EN_US
                );
                if (successMessage == null) {
                    successMessage = "Register successfully"; // 最后回退
                }
            }
            
            logger.debug("User language preference: {}, success message: {}", languageCode, successMessage);
            
            ResponseDTO<User> response = ResponseDTO.success(successMessage, savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error registering user: {} - {}",
                    user.getUsername(), e.getMessage(), e);
            ResponseDTO<User> errorResponse = ResponseDTO.error(ApiConstants.USER_REGISTER_FAILED_MESSAGE);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<LoginResponseDTO.UserData>> loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        logger.info("Received login request for phone: {}", loginRequestDTO.getPhoneNumber());

        // 入参参数校验
        if (loginRequestDTO.getPhoneNumber() == null || loginRequestDTO.getPhoneNumber().trim().isEmpty()) {
            logger.warn("Login failed - Phone number is empty");
            ResponseDTO<LoginResponseDTO.UserData> errorResponse = ResponseDTO.error(ApiConstants.USER_PHONE_EMPTY_MESSAGE);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (loginRequestDTO.getPassword() == null || loginRequestDTO.getPassword().trim().isEmpty()) {
            logger.warn("Login failed - Password is empty for phone: {}", loginRequestDTO.getPhoneNumber());
            ResponseDTO<LoginResponseDTO.UserData> errorResponse = ResponseDTO.error(ApiConstants.USER_PASSWORD_EMPTY_MESSAGE);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            Optional<User> userOptional = userService.getUserByPhoneNumber(loginRequestDTO.getPhoneNumber());
            
            if (!userOptional.isPresent()) {
                logger.warn("Login failed - User not found for phone: {}", loginRequestDTO.getPhoneNumber());
                ResponseDTO<LoginResponseDTO.UserData> errorResponse = ResponseDTO.error(ApiConstants.USER_PHONE_NOT_REGISTERED_MESSAGE);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            User user = userOptional.get();

            // 验证密码（使用加密验证）
            if (!userService.verifyPassword(loginRequestDTO.getPassword(), user.getPassword())) {
                logger.warn("Login failed - Invalid password for phone: {}", loginRequestDTO.getPhoneNumber());
                ResponseDTO<LoginResponseDTO.UserData> errorResponse = ResponseDTO.error(ApiConstants.USER_PASSWORD_INCORRECT_MESSAGE);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getId(), user.getPhoneNumber());
            
            // 从数据库查询用户语言偏好对应的成功消息
            String languageCode = user.getLanguagePreference();
            String successMessage = translationService.getTranslation(
                    TRANSLATIONS_TYPE_SYSTEM_MESSAGE,
                    0L,
                    ApiConstants.TRANSLATIONS_KEY_LOGIN_SUCCESS,
                    languageCode
            );
            
            if (successMessage == null) {
                // 如果没有找到翻译，回退到英文或使用默认值
                successMessage = translationService.getTranslation(
                        TRANSLATIONS_TYPE_SYSTEM_MESSAGE,
                        0L,
                        ApiConstants.TRANSLATIONS_KEY_LOGIN_SUCCESS,
                        ApiConstants.LANGUAGE_EN_US
                );
                if (successMessage == null) {
                    successMessage = "Login successfully"; // 最后回退
                }
            }
            
            logger.debug("User language preference: {}, success message: {}", languageCode, successMessage);
            
            // create return responseDTO
            LoginResponseDTO.UserData userData = new LoginResponseDTO.UserData(
                user.getId(), 
                token
            );
            
            ResponseDTO<LoginResponseDTO.UserData> response = ResponseDTO.success(successMessage, userData);
            
            logger.info("User logged in successfully: {}", user.getUsername());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error during login for phone: {} - {}", 
                    loginRequestDTO.getPhoneNumber(), e.getMessage(), e);
            ResponseDTO<LoginResponseDTO.UserData> errorResponse = ResponseDTO.error(ApiConstants.USER_LOGIN_FAILED_MESSAGE);
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