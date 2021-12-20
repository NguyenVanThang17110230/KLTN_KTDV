package com.document.manager.rest.user;


import com.document.manager.domain.RoleApp;
import com.document.manager.domain.UserApp;
import com.document.manager.domain.UserReference;
import com.document.manager.dto.*;
import com.document.manager.dto.constants.Constants;
import com.document.manager.dto.enums.Gender;
import com.document.manager.dto.mapper.DTOMapper;
import com.document.manager.service.MailService;
import com.document.manager.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
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

    private final PasswordEncoder passwordEncoder;

    private final DTOMapper dtoMapper;

    private final UserService userService;

    private final MailService mailService;

    private final Environment environment;

    private final HttpServletRequest request;

    @GetMapping("/welcome")
    @ApiOperation(value = "API test connection")
    public ResponseEntity<ResponseData> welcome() {
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.toString())
                .message("Welcome to my app")
                .data("Connect success").build(), OK);
    }

    @GetMapping("/data")
    @ApiOperation(value = "API create data default")
    public ResponseEntity<ResponseData> createData() {
        if (userService.findRoleByName(Constants.ROLE_USER) == null) {
            RoleApp roleUser = new RoleApp(null, Constants.ROLE_USER);
            userService.save(roleUser);
        }
        if (userService.findRoleByName(Constants.ROLE_ADMIN) == null) {
            RoleApp roleAdmin = new RoleApp(null, Constants.ROLE_ADMIN);
            userService.save(roleAdmin);
        }

        RoleApp roleAdmin = userService.findRoleByName(Constants.ROLE_ADMIN);
        RoleApp roleUser = userService.findRoleByName(Constants.ROLE_USER);
        if (userService.findByEmail("admin@yopmail.com") == null) {
            List<RoleApp> roles = new ArrayList<>();
            roles.add(roleAdmin);
            roles.add(roleUser);

            userService.save(new UserApp("10000000",
                    "A",
                    "Admin",
                    Gender.MALE,
                    new Date("01/01/1999"),
                    "1111111111",
                    "admin@yopmail.com",
                    passwordEncoder.encode("12345678"),
                    roles));
        }
        if (userService.findByEmail("user@yopmail.com") == null) {
            userService.save(new UserApp("10000001",
                    "U",
                    "User",
                    Gender.MALE,
                    new Date("02/02/2000"),
                    "2222222222",
                    "user@yopmail.com",
                    passwordEncoder.encode("12345678"),
                    new ArrayList<>(Collections.singleton(roleUser))));
        }
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.toString())
                .message("Create data successful").build(), OK);
    }

    @PostMapping(value = "/sign-in")
    @ApiOperation(value = "API sign in")
    public ResponseEntity<ResponseData> signIn(@Validated @RequestBody SignInDTO signInDTO) throws Exception {
        try {
            logger.info("User {} login success", signInDTO.getEmail());
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.toString())
                    .message("Sign in successful")
                    .data(userService.signIn(signInDTO.getEmail(), signInDTO.getPassword())).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.toString())
                    .message("Sign in failed with error: " + e.getMessage()).build(), OK);
        }
    }

    @PostMapping(value = "/sign-up")
    @ApiOperation(value = "API sign up")
    public ResponseEntity<ResponseData> signUp(@Validated @RequestBody SignUpDTO signUpDTO) {
        try {
            UserApp userApp = dtoMapper.toUser(signUpDTO);
            userApp = userService.register(userApp);

            // Send mail
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("link", "www.google.com");
            mailService.sendMailRegister(userApp.getEmail(), userApp.getFirstName() + userApp.getLastName(), mapData);

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
            userService.changePassword(email, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Change password success").build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PermitAll
    @PostMapping(value = "/forgot-password")
    @ApiOperation(value = "API send request forgot password")
    public ResponseEntity<ResponseData> forgotPassword(@RequestParam("email") String email) {
        if (StringUtils.isEmpty(email)) {
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
            mailService.sendMailForgotPassword(userApp.getEmail(), userApp.getFirstName() + userApp.getLastName(), mapData);
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
        if (StringUtils.isEmpty(email)) {
            logger.error("Email is empty", email);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("Email not allow empty").build(), BAD_REQUEST);
        }
        if (StringUtils.isEmpty(uuid)) {
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
        if (StringUtils.isEmpty(email)) {
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
            mailService.sendMailForgotPassword(userApp.getEmail(), userApp.getFirstName() + userApp.getLastName(), mapData);
        }
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Resend link to success").build(), OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ApiOperation(value = "API get all users in system      (Role: Admin)")
    public ResponseEntity<ResponseData> getUsers() {
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Get all users success")
                .data(userService.getUsers()).build(), OK);
    }

    @GetMapping(value = "/{email}")
    @ApiOperation(value = "API get user by email")
    public ResponseEntity<ResponseData> getUserByEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Get user by email success")
                .data(userService.getUserByEmail(email)).build(), OK);
    }

    @PatchMapping(value = "/{id}")
    @ApiOperation(value = "API update user information")
    public ResponseEntity<ResponseData> updateUserInfo(@PathVariable("id") Long userId, @RequestBody UserInfoDTO userInfoDTO) {
        try {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Update user information successful")
                    .data(dtoMapper.toUserAppDTO(userService.updateUserInfo(userId, userInfoDTO))).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping(value = "/lock/{id}")
    @ApiOperation(value = "API lock account (set is_active = false)         (Role: Admin)")
    public ResponseEntity<ResponseData> lockAccount(@PathVariable("id") Long userId) {
        UserApp userApp = userService.findUserById(userId);
        if (userApp == null) {
            logger.error("User with id {} not found", userId);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("User with id " + userId + " not found").build(), BAD_REQUEST);
        }
        userApp.setIsActive(Boolean.FALSE);
        userService.save(userApp);
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Locked account success").build(), OK);
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "API get information of user current")
    public ResponseEntity<ResponseData> getUserInfoCurrent() {
        try {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Get user info current success")
                    .data(dtoMapper.toUserAppDTO(userService.getCurrentUser())).build(), OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("User current not found").build(), BAD_REQUEST);
        }
    }

    @PostMapping(value = "/avatar")
    @ApiOperation(value = "API change avatar of user")
    public ResponseEntity<ResponseData> changeAvatar(@RequestParam(value = "file") MultipartFile file) {
        try {
            userService.changeAvatar(file);
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Change avatar successful").build(), OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Change avatar failed").build(), BAD_REQUEST);
        }
    }

    @PostMapping(value = "/token/refresh")
    @ApiOperation(value = "API to get access token from refresh token")
    public ResponseEntity<ResponseData> refreshToken(@Validated @RequestBody AuthorizationDTO authorizationDTO) {
        try {
            return new ResponseEntity<>(ResponseData.builder().status(SUCCESS.name())
                    .message("Refresh token successful").data(userService
                            .refreshToken(authorizationDTO.getAuthorization())).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder().status(ERROR.name())
                    .message(e.getMessage()).build(), BAD_REQUEST);
        }
    }

    @GetMapping(value = "/logout")
    @ApiOperation(value = "API to log out")
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
