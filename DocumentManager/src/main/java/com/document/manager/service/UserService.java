package com.document.manager.service;

import com.document.manager.domain.RoleApp;
import com.document.manager.domain.UserApp;
import com.document.manager.domain.UserReference;
import com.document.manager.dto.ChangePasswordDTO;
import com.document.manager.dto.UserInfoDTO;

import java.util.List;

public interface UserService {

    UserApp findUserById(Long id);

    UserApp save(UserApp userApp) throws IllegalArgumentException;

    RoleApp save(RoleApp role);

    RoleApp findRoleByName(String roleName) throws IllegalArgumentException;

    UserApp findByEmail(String email) throws IllegalArgumentException;

    void changePassword(String email, ChangePasswordDTO changePasswordDTO) throws IllegalArgumentException;

    UserReference save(UserReference userReference);

    UserReference findByUuid(String uuid);

    List<UserReference> findUserReferenceByEmail(String email);

    boolean delete(UserReference userReference);

    List<UserApp> getUsers();

    UserApp getUserById(Long id);

    UserApp getUserByEmail(String email);

    UserApp updateUserInfo(UserInfoDTO userInfoDTO, UserApp userApp);

    List<RoleApp> getRoles(Long userId);
}
