package com.document.manager.rest;


import com.document.manager.domain.UserApp;
import com.document.manager.domain.UserReference;
import com.document.manager.dto.ChangePasswordDTO;
import com.document.manager.dto.ResetPasswordDTO;
import com.document.manager.dto.SignInDTO;
import com.document.manager.dto.SignUpDTO;
import com.document.manager.dto.mapper.DTOMapper;
import com.document.manager.jwt.JwtTokenProvider;
import com.document.manager.service.UserService;
import com.document.manager.service.impl.MailService;
import com.document.manager.util.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.document.manager.dto.enums.ResponseDataStatus.ERROR;
import static com.document.manager.dto.enums.ResponseDataStatus.SUCCESS;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/api/user")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "/api/user", tags = "User Controller")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final DTOMapper dtoMapper;

    private final UserService userService;

    private final MailService mailService;


    @GetMapping("/welcome")
    @ApiOperation(value = "API test connection")
    public ResponseEntity<ResponseData> welcome() {
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.toString())
                .message("Welcome to my app")
                .data("Connect success").build(), OK);
    }

    @PostMapping(value = "/sign-in")
    @ApiOperation(value = "API sign in")
    public ResponseEntity<ResponseData> signIn(@Validated @RequestBody SignInDTO signInDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getEmail(), signInDTO.getPassword()));
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(user);
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("jwt", jwt);
        mapData.put("roles", user.getAuthorities());
        ResponseData responseData = ResponseData.builder()
                .status(SUCCESS.toString())
                .message("Sign in successful")
                .data(mapData).build();
        logger.info("User {} login success", signInDTO.getEmail());
        return new ResponseEntity<>(responseData, OK);
    }

    @PostMapping(value = "/sign-up")
    @ApiOperation(value = "API sign up")
    public ResponseEntity<ResponseData> signUp(@Validated @RequestBody SignUpDTO signUpDTO) {
        try {
            UserApp userApp = dtoMapper.toUser(signUpDTO);
            logger.info("Convert to entity success");
            userApp = userService.save(userApp);
            logger.info("Save user entity success");

            // Send mail
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("link", "www.google.com");
            mailService.sendMailRegister(userApp.getEmail(), userApp.getFirstname() + userApp.getLastname(), mapData);

            logger.info("User {} sign up success", userApp.getEmail(), OK);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.toString())
                    .message("Sign up successful")
                    .data(userApp).build(), OK);

        } catch (Exception e) {
            logger.info("User sign up failed");
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("Sign up failed").build(), CONFLICT);
        }
    }

    @PatchMapping(value = "/change-password")
    @ApiOperation(value = "API change password")
    public ResponseEntity<ResponseData> changePassword(@Validated @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user == null) {
                return new ResponseEntity<>(ResponseData.builder()
                        .status(ERROR.name())
                        .message("Email of user current not found").build(), BAD_REQUEST);
            }
            String email = user.getUsername();
            userService.changePassword(email, changePasswordDTO);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Change password success").build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PostMapping(value = "/forgot-password")
    @ApiOperation(value = "API send request forgot password")
    public ResponseEntity<ResponseData> forgotPassword(@RequestParam("email") String email) {
        if (GenericValidator.isBlankOrNull(email)) {
            logger.error("Email is empty");
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("Email is not allow empty").build(), BAD_REQUEST);
        }
        UserApp userApp = userService.findByEmail(email);
        if (userApp == null) {
            logger.error("User {} not found", email);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("User not found").build(), NOT_FOUND);
        }
        // Handle link for reset password
        String uuid = UUID.randomUUID().toString();
        UserReference userReference = UserReference
                .builder().uuid(uuid)
                .userApp(userApp)
                .createdStamp(new Date()).build();

        userReference = userService.save(userReference);

        if (userReference != null) {
            // Send mail
            Map<String, Object> mapData = new HashMap<>();
            String link = "http://localhost:9000/reset-password?email=" + userReference.getUserApp().getEmail() + "&uuid=" + uuid;
            mapData.put("link", link);
            mailService.sendMailForgotPassword(userApp.getEmail(), userApp.getFirstname() + userApp.getLastname(), mapData);
        }

        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Send request forgot password success").build(), OK);
    }

    @PostMapping(value = "/reset-password")
    @ApiOperation(value = "API reset password")
    public ResponseEntity<ResponseData> resetPassword(@RequestParam("email") String email,
                                                      @RequestParam("uuid") String uuid,
                                                      @Validated @RequestBody ResetPasswordDTO resetPasswordDTO) {
        if (GenericValidator.isBlankOrNull(email)) {
            logger.error("Email is empty", email);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("Email not allow empty").build(), BAD_REQUEST);
        }
        if (GenericValidator.isBlankOrNull(uuid)) {
            logger.error("Code is empty", email);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("Code not allow empty").build(), BAD_REQUEST);
        }
        UserApp userApp = userService.findByEmail(email);
        if (userApp == null) {
            logger.error("User {} not found", email);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("User not found").build(), NOT_FOUND);
        }
        // Handle reset password
        UserReference userReference = userService.findByUuid(uuid);
        if (userReference == null) {
            logger.error("User reference with uuid {} not found", uuid);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("User reference not found").build(), NOT_FOUND);
        }
        if (new Date().after(userReference.getExpiredStamp())) {
            logger.error("User reference with uuid {} was expired", uuid);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("User reference was expired").build(), BAD_REQUEST);
        }
        if (!resetPasswordDTO.getPassword().equals(resetPasswordDTO.getConfirmPassword())) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("New password and confirm password is not match").build(), BAD_REQUEST);
        }
        userApp.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        userService.save(userApp);
        logger.info("Reset password successful");
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Reset password successful").build(), OK);
    }

    @PostMapping(value = "/resend-email")
    @ApiOperation(value = "API resend email when request forgot password")
    public ResponseEntity<ResponseData> resendEmail(@RequestParam("email") String email) {
        if (GenericValidator.isBlankOrNull(email)) {
            logger.info("Email is empty");
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("Email not allow empty").build(), BAD_REQUEST);
        }
        UserApp userApp = userService.findByEmail(email);
        if (userApp == null) {
            logger.info("User {} not found", email);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("User not found").build(), BAD_REQUEST);
        }
        List<UserReference> userReferences = userService.findUserReferenceByEmail(email);
        if (userReferences != null && userReferences.size() > 0) {
            UserReference userReference = userReferences.stream().sorted().findFirst().get();
            userService.delete(userReference);
        }
        String uuid = UUID.randomUUID().toString();
        UserReference userReference = UserReference
                .builder().uuid(uuid)
                .userApp(userApp)
                .createdStamp(new Date()).build();

        userReference = userService.save(userReference);

        if (userReference != null) {
            // Send mail
            Map<String, Object> mapData = new HashMap<>();
            String link = "http://localhost:9000/reset-password?email=" + userReference.getUserApp().getEmail() + "&uuid=" + uuid;
            mapData.put("link", link);
            mailService.sendMailForgotPassword(userApp.getEmail(), userApp.getFirstname() + userApp.getLastname(), mapData);
        }
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Resend link to success").build(), OK);
    }

    @GetMapping(value = "/users")
    @ApiOperation(value = "API get all users in system")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseData> getUsers() {
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Get all users success")
                .data(userService.getUsers()).build(), OK);
    }

    @GetMapping(value = "/users/{id}")
    @ApiOperation(value = "API get user by id")
    public ResponseEntity<ResponseData> getUserById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Get all users success")
                .data(userService.getUserById(id)).build(), OK);
    }

    @GetMapping(value = "/users/{email}")
    @ApiOperation(value = "API get user by email")
    public ResponseEntity<ResponseData> getUserByEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Get all users success")
                .data(userService.getUserByEmail(email)).build(), OK);
    }
}
