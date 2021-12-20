package com.document.manager.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.document.manager.domain.RoleApp;
import com.document.manager.domain.UserApp;
import com.document.manager.domain.UserReference;
import com.document.manager.dto.AuthorizationDTO;
import com.document.manager.dto.ResponseData;
import com.document.manager.dto.UserInfoDTO;
import com.document.manager.dto.constants.Constants;
import com.document.manager.repository.RoleRepo;
import com.document.manager.repository.UserReferenceRepo;
import com.document.manager.repository.UserRepo;
import com.document.manager.service.FileService;
import com.document.manager.service.UserService;
import javassist.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.document.manager.dto.enums.Gender.FEMALE;
import static com.document.manager.dto.enums.Gender.MALE;
import static com.document.manager.dto.enums.ResponseDataStatus.ERROR;
import static com.document.manager.dto.enums.ResponseDataStatus.SUCCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserReferenceRepo userReferenceRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private FileService fileService;

    @Autowired
    private Environment environment;

    @Autowired
    private HttpServletRequest request;


    @Override
    public UserDetails loadUserByUsername(String username) {
        UserApp userApp = userRepo.findByEmail(username);
        if (userApp == null) {
            logger.error("User {} not found", username);
            return null;
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userApp.getRoleApps().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(userApp.getEmail(), userApp.getPassword(), authorities);
    }

    @Override
    public UserApp findUserById(Long id) {
        Optional<UserApp> userOptional = userRepo.findById(id);
        if (userOptional.isPresent()) {
            logger.info("User with id {} found", id);
            return userOptional.get();
        }
        logger.error("User with id {} not fount", id);
        return null;
    }

    @Override
    public UserApp save(UserApp userApp) throws IllegalArgumentException {
        logger.info("Saving new user {} to the database", userApp.getEmail());
        return userRepo.save(userApp);
    }

    @Override
    public UserApp register(UserApp userApp) throws IllegalArgumentException {
        if (userApp == null) {
            return null;
        }
        if (userRepo.findByEmail(userApp.getEmail()) != null) {
            logger.error("Email {} already exist in database", userApp.getEmail());
            throw new IllegalArgumentException("Email already exist");
        }
        if (userApp.getRoleApps() == null || userApp.getRoleApps().size() <= 0) {
            RoleApp roleUser = roleRepo.findByName(Constants.ROLE_USER);
            if (roleUser != null) {
                userApp.setRoleApps(new ArrayList<>(Collections.singleton(roleUser)));
            }
        }
        userApp.setPassword(passwordEncoder.encode(userApp.getPassword()));
        return this.save(userApp);
    }

    @Override
    public RoleApp save(RoleApp role) {
        logger.info("Saving new role {} to the database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public RoleApp findRoleByName(String roleName) throws IllegalArgumentException {
        if (StringUtils.isEmpty(roleName)) {
            logger.error("Role name is empty");
            throw new IllegalArgumentException("Role name not allowed empty");
        }
        RoleApp role = roleRepo.findByName(roleName);
        if (role == null) {
            logger.error("Role {} not found", roleName);
            return null;
        }
        logger.info("Role {} found", roleRepo);
        return role;
    }

    @Override
    public UserApp findByEmail(String email) throws IllegalArgumentException {
        if (StringUtils.isEmpty(email)) {
            logger.error("Email is empty");
            throw new IllegalArgumentException("Emails are not allowed to be empty");
        }
        UserApp userApp = userRepo.findByEmail(email);
        if (userApp == null) {
            logger.error("User with email {} not found", email);
            return null;
        }
        logger.info("User with email {} found", email);
        return userApp;
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) throws IllegalArgumentException {
        if (StringUtils.isEmpty(email)) {
            logger.error("Emails are not allowed to be empty");
            throw new IllegalArgumentException("Emails are not allowed to be empty");
        }
        UserApp userApp = findByEmail(email);
        if (!passwordEncoder.matches(oldPassword, userApp.getPassword())) {
            logger.error("Old password is incorrect");
            throw new IllegalArgumentException("Old password is incorrect");
        }
        userApp.setPassword(passwordEncoder.encode(newPassword));
        logger.info("Change password for user {} success", userApp.getEmail());
        save(userApp);
    }

    @Override
    public UserReference save(UserReference userReference) {
        LocalDateTime now = LocalDateTime.ofInstant(userReference.getCreatedStamp().toInstant(), ZoneId.systemDefault());
        now.plusMinutes(15);
        userReference.setExpiredStamp(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
        logger.info("Saving new user reference with uuid {} for user {} to the database",
                userReference.getUuid(), userReference.getUserApp().getEmail());
        return userReferenceRepo.save(userReference);
    }

    @Override
    public UserReference findByUuid(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            logger.error("Uuid is empty");
            return null;
        }
        UserReference userReference = userReferenceRepo.findByUuid(uuid);
        if (userReference == null) {
            logger.error("User reference with uuid {} not found", uuid);
            return null;
        }
        logger.info("User reference with uuid {} found", uuid);
        return userReference;
    }

    @Override
    public List<UserReference> findUserReferenceByEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
//        return userReferenceRepo.findByEmail(email);
    }

    @Override
    public boolean delete(UserReference userReference) {
        if (userReference == null) {
            logger.info("Delete user reference success");
            return false;
        }
        logger.info("Delete user reference success");
        userReferenceRepo.delete(userReference);
        return true;
    }

    @Override
    public List<UserApp> getUsers() {
        logger.info("Get all users");
        return userRepo.findAll();
    }

    @Override
    public UserApp getUserById(Long id) {
        Optional<UserApp> userApp = userRepo.findById(id);
        if (!userApp.isPresent()) {
            logger.info("User with id {} not found", id);
            return null;
        }
        logger.info("User with id {} found", id);
        return userApp.get();
    }

    @Override
    public UserApp getUserByEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            logger.error("Email is empty");
            return null;
        }
        UserApp userApp = userRepo.findByEmail(email);
        if (userApp == null) {
            logger.error("User with email {} not found", email);
            return null;
        }
        logger.info("User with email {} found", email);
        return userApp;
    }

    @Override
    public UserApp updateUserInfo(UserInfoDTO userInfoDTO, UserApp userApp) {
        if (userInfoDTO == null || userApp == null) {
            logger.error("Data invalid");
            return null;
        }
        if (!StringUtils.isEmpty(userInfoDTO.getUserCode())) {
            userApp.setUserCode(userInfoDTO.getUserCode());
        }
        if (!StringUtils.isEmpty(userInfoDTO.getFirstName())) {
            userApp.setFirstName(userInfoDTO.getFirstName());
        }
        if (!StringUtils.isEmpty(userInfoDTO.getLastName())) {
            userApp.setLastName(userInfoDTO.getLastName());
        }
        if (!StringUtils.isEmpty(userInfoDTO.getGender())) {
            userApp.setGender(userInfoDTO.getGender().equalsIgnoreCase(MALE.toString()) ? MALE : FEMALE);
        }
        if (!StringUtils.isEmpty(userInfoDTO.getPhoneNumber())) {
            userApp.setPhoneNumber(userInfoDTO.getPhoneNumber());
        }
        logger.error("Update user info success");
        return save(userApp);
    }

    @Override
    public UserApp updateUserInfo(Long userId, UserInfoDTO userInfoDTO) throws Exception {
        UserApp userApp = this.findUserById(userId);
        if (userApp == null) {
            logger.error("User with id {} not found", userId);
            throw new NotFoundException("User with id " + userId + " not found");
        }
        if (userInfoDTO == null) {
            return userApp;
        }
        if (!StringUtils.isEmpty(userInfoDTO.getUserCode())) {
            userApp.setUserCode(userInfoDTO.getUserCode());
        }
        if (!StringUtils.isEmpty(userInfoDTO.getFirstName())) {
            userApp.setFirstName(userInfoDTO.getFirstName());
        }
        if (!StringUtils.isEmpty(userInfoDTO.getLastName())) {
            userApp.setLastName(userInfoDTO.getLastName());
        }
        if (!StringUtils.isEmpty(userInfoDTO.getGender())) {
            userApp.setGender(userInfoDTO.getGender().equalsIgnoreCase(MALE.toString()) ? MALE : FEMALE);
        }
        if (!StringUtils.isEmpty(userInfoDTO.getPhoneNumber())) {
            userApp.setPhoneNumber(userInfoDTO.getPhoneNumber());
        }
        logger.error("Update user info success");
        return save(userApp);
    }

    @Override
    public List<RoleApp> getRoles(Long userId) {
        List<RoleApp> roleApps = new ArrayList<>();
//        if (usersRoles == null || usersRoles.size() <= 0) {
//            return roleApps;
//        }
//        for (UsersRoles userRole : usersRoles) {
//            Optional<RoleApp> roleApp = roleRepo.findById(userRole.getRoleApp().getId());
//            if (roleApp.isPresent()) {
//                roleApps.add(roleApp.get());
//            }
//        }
        return roleApps;
    }

    @Override
    public Map<String, Object> signIn(String email, String password) throws Exception {
        UserApp userApp = this.getUserByEmail(email);
        if (userApp == null) {
            throw new NotFoundException("User " + email + " not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userApp.getRoleApps().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        Algorithm algorithm = Algorithm.HMAC256(environment.getProperty("jwt.secret").getBytes());
        String access_token = JWT.create()
                .withSubject(email)
                .withIssuer(request.getRequestURL().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(environment.getProperty("jwt.access.token.expire"))))
                .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(email)
                .withIssuer(request.getRequestURL().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(environment.getProperty("jwt.refresh.token.expire"))))
                .sign(algorithm);
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("access_token", access_token);
        mapData.put("refresh_token", refresh_token);
        mapData.put("roles", userApp.getRoleApps().stream().map(RoleApp::getName).collect(Collectors.toList()));
        return mapData;
    }

    @Override
    public void changeAvatar(MultipartFile file) throws NotFoundException {
        if (file == null) {
            throw new NotFoundException("Avatar not found");
        }
        try {
            UserApp userApp = getCurrentUser();
            userApp.setAvatar(file.getOriginalFilename());
            fileService.saveFile(Constants.DIR_UPLOADED_USER, file.getOriginalFilename(), file.getBytes());
            this.save(userApp);
        } catch (NotFoundException | IOException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public UserApp getCurrentUser() throws NotFoundException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            UserApp userApp = this.findByEmail(email);
            if (userApp == null) {
                throw new NotFoundException("Current user not found");
            }
            return userApp;
        } catch (Exception e) {
            throw new NotFoundException("Current user not found");
        }
    }

    @Override
    public Map<String, String> refreshToken(String authorization) {
        if (StringUtils.isEmpty(authorization) || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Refresh token is missing");
        }
        try {
            String refresh_token = authorization.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256(environment.getProperty("jwt.secret").getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);
            String email = decodedJWT.getSubject();
            UserApp user = this.findByEmail(email);
            String access_token = JWT.create()
                    .withSubject(user.getEmail())
                    .withExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(environment.getProperty("jwt.access.token.expire"))))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles", user.getRoleApps().stream().map(RoleApp::getName).collect(Collectors.toList()))
                    .sign(algorithm);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", access_token);
            return tokens;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}