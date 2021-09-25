package com.document.manager.service;

import com.document.manager.domain.Role;
import com.document.manager.domain.UserApp;
import com.document.manager.domain.UserReference;
import com.document.manager.dto.ChangePasswordDTO;

import java.util.List;

public interface UserService {

    UserApp findUserById(Long id);

    UserApp save(UserApp userApp) throws IllegalArgumentException;

    Role save(Role role);

    Role findRoleByName(String roleName) throws IllegalArgumentException;

    UserApp findByEmail(String email) throws IllegalArgumentException;

    void changePassword(String email, ChangePasswordDTO changePasswordDTO) throws IllegalArgumentException;

    UserReference save(UserReference userReference);

    UserReference findByUuid(String uuid);

    List<UserReference> findUserReferenceByEmail(String email);

    boolean delete(UserReference userReference);

    List<UserApp> getUsers();

    UserApp getUserById(Long id);

    UserApp getUserByEmail(String email);
}
