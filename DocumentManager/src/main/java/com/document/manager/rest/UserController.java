package com.document.manager.rest;


import com.document.manager.domain.User;
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
import lombok.AllArgsConstructor;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final DTOMapper dtoMapper;

    private final UserService userService;

    private final MailService mailService;


    @PostMapping(value = "/sign-in")
    public ResponseEntity<ResponseData> signIn(@Validated @RequestBody SignInDTO signInDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getUsername(), signInDTO.getPassword()));
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(user);
        ResponseData responseData = ResponseData.builder()
                .status(SUCCESS.toString())
                .message("Sign in successful")
                .data(jwt).build();
        logger.info("User {} login success", signInDTO.getUsername());
        return new ResponseEntity<>(responseData, OK);
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<ResponseData> signUp(@Validated @RequestBody SignUpDTO signUpDTO) {
        try {
            User user = dtoMapper.toUser(signUpDTO);
            user = userService.save(user);

            // Send mail
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("link", "www.google.com");
            mailService.sendMailRegister(user.getEmail(), user.getFirstname() + user.getLastname(), mapData);

            logger.info("User {} sign up success", user.getEmail(), OK);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.toString())
                    .message("Sign up successful")
                    .data(user).build(), OK);

        } catch (Exception e) {
            logger.info("User sign up failed");
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("Sign up failed").build(), CONFLICT);
        }
    }

    @PatchMapping(value = "/change-password")
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
    public ResponseEntity<ResponseData> forgotPassword(@RequestParam("email") String email) {
        if (GenericValidator.isBlankOrNull(email)) {
            logger.error("Email is empty");
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("Email is not allow empty").build(), BAD_REQUEST);
        }
        User user = userService.findByEmail(email);
        if (user == null) {
            logger.error("User {} not found", email);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("User not found").build(), NOT_FOUND);
        }
        // Handle link for reset password
        String uuid = UUID.randomUUID().toString();
        UserReference userReference = UserReference
                .builder().uuid(uuid)
                .user(user)
                .createdStamp(new Date()).build();

        userReference = userService.save(userReference);

        if (userReference != null) {
            // Send mail
            Map<String, Object> mapData = new HashMap<>();
            String link = "http://localhost:9000/reset-password?email=" + userReference.getUser().getEmail() + "&uuid=" + uuid;
            mapData.put("link", link);
            mailService.sendMailForgotPassword(user.getEmail(), user.getFirstname() + user.getLastname(), mapData);
        }

        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Send request forgot password success").build(), OK);
    }

    @PostMapping(value = "/reset-password")
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
        User user = userService.findByEmail(email);
        if (user == null) {
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
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        userService.save(user);
        logger.info("Reset password successful");
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Reset password successful").build(), OK);
    }

    @PostMapping(value = "/resend-email")
    public ResponseEntity<ResponseData> resendEmail(@RequestParam("email") String email) {
        if (GenericValidator.isBlankOrNull(email)) {
            logger.info("Email is empty");
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("Email not allow empty").build(), BAD_REQUEST);
        }
        User user = userService.findByEmail(email);
        if (user == null) {
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
                .user(user)
                .createdStamp(new Date()).build();

        userReference = userService.save(userReference);

        if (userReference != null) {
            // Send mail
            Map<String, Object> mapData = new HashMap<>();
            String link = "http://localhost:9000/reset-password?email=" + userReference.getUser().getEmail() + "&uuid=" + uuid;
            mapData.put("link", link);
            mailService.sendMailForgotPassword(user.getEmail(), user.getFirstname() + user.getLastname(), mapData);
        }
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Resend link to success").build(), OK);
    }
}
