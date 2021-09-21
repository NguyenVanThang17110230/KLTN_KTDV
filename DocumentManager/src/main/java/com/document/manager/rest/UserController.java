package com.document.manager.rest;


import com.document.manager.domain.User;
import com.document.manager.dto.SignInDTO;
import com.document.manager.dto.SignUpDTO;
import com.document.manager.dto.mapper.DTOMapper;
import com.document.manager.jwt.JwtTokenProvider;
import com.document.manager.service.UserService;
import com.document.manager.util.ResponseData;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.document.manager.dto.enums.ResponseDataStatus.SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/api/user")
@AllArgsConstructor
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final DTOMapper dtoMapper;

    private final UserService userService;

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
    public ResponseEntity<ResponseData> signUp(@Validated @RequestBody SignUpDTO signUpDTO) throws IllegalArgumentException {
        User user = dtoMapper.toUser(signUpDTO);
        user = userService.save(user);
        ResponseData responseData = ResponseData.builder()
                .status(SUCCESS.toString())
                .message("Sign in successful")
                .data(user).build();
        logger.info("User {} sign up success", user.getUsername());
        return new ResponseEntity<>(responseData, OK);
    }
}
