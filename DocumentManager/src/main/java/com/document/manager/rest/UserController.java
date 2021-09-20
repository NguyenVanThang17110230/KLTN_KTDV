package com.document.manager.rest;


import com.document.manager.dto.SignInDTO;
import com.document.manager.dto.SignUpDTO;
import com.document.manager.jwt.JwtTokenProvider;
import com.document.manager.util.ResponseData;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.document.manager.dto.enums.ResponseDataStatus.ERROR;
import static com.document.manager.dto.enums.ResponseDataStatus.SUCCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/api/user")
@AllArgsConstructor
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    @PostMapping(value = "/sign-in")
    public ResponseEntity<ResponseData> signIn(@Validated @RequestBody SignInDTO signInDTO) {
//        if (signInDTO == null) {
//            logger.error("Data is empty");
//            return new ResponseEntity<>(new ResponseData(ERROR.toString(), "Data is empty", null), BAD_REQUEST);
//        }
//        if (GenericValidator.isBlankOrNull(signInDTO.getUsername()) || GenericValidator.isBlankOrNull(signInDTO.getPassword())) {
//            logger.error("Username or password is empty");
//            return new ResponseEntity<>(new ResponseData(ERROR.toString(),
//                    "Username or password is empty",
//                    null),
//                    BAD_REQUEST);
//        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getUsername(), signInDTO.getPassword()));
        User user = (User) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(user);
        ResponseData responseData = ResponseData.builder()
                .status(SUCCESS.toString())
                .message("Sign in successful")
                .data(jwt).build();
        logger.info("User {} login success", signInDTO.getUsername());
        return new ResponseEntity<>(responseData, OK);
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<ResponseData> signUp(@RequestBody SignUpDTO signUpDTO) {
        if (signUpDTO == null) {
            logger.error("Data is empty");
            return new ResponseEntity<>(new ResponseData(ERROR.toString(), "Data is empty", null), BAD_REQUEST);
        }


        return null;
    }
}
