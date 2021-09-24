package com.document.manager.service.impl;

import com.document.manager.domain.Role;
import com.document.manager.domain.User;
import com.document.manager.domain.UserReference;
import com.document.manager.dto.ChangePasswordDTO;
import com.document.manager.repository.RoleRepo;
import com.document.manager.repository.UserReferenceRepo;
import com.document.manager.repository.UserRepo;
import com.document.manager.service.UserService;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.document.manager.dto.constants.Constants.ROLE_USER;

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


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByEmail(username);
        if (user == null) {
            logger.error("User {} not found", username);
            return null;
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isPresent()) {
            logger.info("User with id {} found", id);
            return userOptional.get();
        }
        logger.error("User with id {} not fount", id);
        return null;
    }

    @Override
    public User save(User user) throws IllegalArgumentException {
        if (userRepo.findByEmail(user.getEmail()) != null) {
            logger.error("Email {} already exist in database", user.getEmail());
            throw new IllegalArgumentException("Email already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = findRoleByName(ROLE_USER);
        if (role == null) {
            role = roleRepo.save(new Role(null, ROLE_USER));
        }
        user.setRoles(new ArrayList<>(Collections.singleton(role)));
        logger.info("Saving new user {} to the database", user.getEmail());
        return userRepo.save(user);
    }

    @Override
    public Role save(Role role) {
        logger.info("Saving new role {} to the database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public Role findRoleByName(String roleName) throws IllegalArgumentException {
        if (GenericValidator.isBlankOrNull(roleName)) {
            logger.error("Role name is empty");
            throw new IllegalArgumentException("Role name not allowed empty");
        }
        Role role = roleRepo.findByName(roleName);
        if (role == null) {
            logger.error("Role {} not found", roleName);
            return null;
        }
        logger.info("Role {} found", roleRepo);
        return role;
    }

    @Override
    public User findByEmail(String email) throws IllegalArgumentException {
        if (GenericValidator.isBlankOrNull(email)) {
            logger.error("Email is empty");
            throw new IllegalArgumentException("Emails are not allowed to be empty");
        }
        User user = userRepo.findByEmail(email);
        if (user == null) {
            logger.error("User with email {} not found", email);
            return null;
        }
        logger.info("User with email {} found", email);
        return user;
    }

    @Override
    public void changePassword(String email, ChangePasswordDTO changePasswordDTO) throws IllegalArgumentException {
        if (GenericValidator.isBlankOrNull(email)) {
            logger.error("Emails are not allowed to be empty");
            throw new IllegalArgumentException("Emails are not allowed to be empty");
        }
        User user = findByEmail(email);
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            logger.error("Old password is incorrect");
            throw new IllegalArgumentException("Old password is incorrect");
        }
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            logger.error("New password and confirm password not match");
            throw new IllegalArgumentException("New password and confirm password not match");
        }
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        logger.info("Change password for user {} success", user.getEmail());
        save(user);
    }

    @Override
    public UserReference save(UserReference userReference) {
        LocalDateTime now = LocalDateTime.ofInstant(userReference.getCreatedStamp().toInstant(), ZoneId.systemDefault());
        now.plusMinutes(15);
        userReference.setExpiredStamp(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
        logger.info("Saving new user reference with uuid {} for user {} to the database",
                userReference.getUuid(), userReference.getUser().getEmail());
        return userReferenceRepo.save(userReference);
    }

    @Override
    public UserReference findByUuid(String uuid) {
        if (GenericValidator.isBlankOrNull(uuid)) {
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
        if (GenericValidator.isBlankOrNull(email)) {
            return new ArrayList<>();
        }
        return userReferenceRepo.findByEmail(email);
    }

    @Override
    public boolean delete(UserReference userReference) {
        if (userReference == null)  {
            logger.info("Delete user reference success");
            return false;
        }
        logger.info("Delete user reference success");
        userReferenceRepo.delete(userReference);
        return true;
    }
}