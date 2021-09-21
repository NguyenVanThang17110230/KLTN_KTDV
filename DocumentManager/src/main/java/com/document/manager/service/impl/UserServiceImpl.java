package com.document.manager.service.impl;

import com.document.manager.domain.Role;
import com.document.manager.domain.User;
import com.document.manager.repository.RoleRepo;
import com.document.manager.repository.UserRepo;
import com.document.manager.service.UserService;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

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
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            logger.error("User {} not found", username);
            throw new UsernameNotFoundException("User not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
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
        if (userRepo.findByUsername(user.getUsername()) != null) {
            logger.error("Username {} already exist in database", user.getUsername());
            throw new IllegalArgumentException("Username already exist");
        }
        if (userRepo.findByEmail(user.getEmail()) != null) {
            logger.error("Email {} already exist in database", user.getUsername());
            throw new IllegalArgumentException("Email already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = findRoleByName(ROLE_USER);
        if (role == null) {
            role = roleRepo.save(new Role(null, ROLE_USER));
        }
        user.setRoles(new ArrayList<>(Collections.singleton(role)));
        logger.info("Saving new user {} to the database", user.getUsername());
        return userRepo.save(user);
    }

    @Override
    public Role save(Role role) {
        logger.info("Saving new role {} to the database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public Role findRoleByName(String roleName) {
        if (GenericValidator.isBlankOrNull(roleName)) {
            return null;
        }
        Role role = roleRepo.findByName(roleName);
        if (role == null) {
            logger.error("Role {} not found", roleName);
            return null;
        }
        logger.info("Role {} found", roleRepo);
        return role;
    }
}