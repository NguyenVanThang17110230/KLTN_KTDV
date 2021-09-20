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
import java.util.Optional;


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
    public User save(User user) {
        logger.info("Saving new user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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