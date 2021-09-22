package com.document.manager.rest;


import com.document.manager.domain.User;
import com.document.manager.dto.ChangePasswordDTO;
import com.document.manager.dto.MailRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
            MailRequest mailRequest = new MailRequest("Nguyễn Văn Hà",
                    "nguyenvanha11899@gmail.com",
                    "vanha.br@gmail.com",
                    "Test send mail");

            Map<String, Object> mapData = new HashMap<>();
            mapData.put("Name", mailRequest.getName());
            mapData.put("location", "Hồ Chí Minh, Việt Nam");
            mailService.sendMail(mailRequest, mapData);
            // End send mail

            logger.info("User {} sign up success", user.getUsername(), OK);
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
    public ResponseEntity<ResponseData> changePassword(@RequestParam("email") String email, @Validated @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
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

    @PostMapping(value = "/reset-password")
    public ResponseEntity<ResponseData> resetPassword(@RequestParam("email")) {


        return null;
    }
}
