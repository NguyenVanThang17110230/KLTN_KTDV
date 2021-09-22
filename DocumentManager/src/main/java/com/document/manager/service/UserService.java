package com.document.manager.service;

import com.document.manager.domain.Role;
import com.document.manager.domain.User;
import com.document.manager.dto.ChangePasswordDTO;
import javassist.NotFoundException;

public interface UserService {

    User findUserById(Long id);

    User save(User user) throws IllegalArgumentException, NotFoundException;

    Role save (Role role);

    Role findRoleByName(String roleName) throws IllegalArgumentException, NotFoundException;

    User findByEmail(String email) throws IllegalArgumentException, NotFoundException;

    void changePassword(String email, ChangePasswordDTO changePasswordDTO) throws IllegalArgumentException, NotFoundException;
}
