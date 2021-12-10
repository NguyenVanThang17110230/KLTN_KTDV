package com.document.manager.service;

import com.document.manager.domain.RoleApp;
import com.document.manager.domain.UserApp;
import com.document.manager.domain.UserReference;
import com.document.manager.dto.UserInfoDTO;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserApp findUserById(Long id);

    UserApp save(UserApp userApp) throws IllegalArgumentException;

    UserApp register(UserApp userApp) throws IllegalArgumentException;

    RoleApp save(RoleApp role);

    RoleApp findRoleByName(String roleName) throws IllegalArgumentException;

    UserApp findByEmail(String email) throws IllegalArgumentException;

    void changePassword(String email, String oldPassword, String newPassword) throws IllegalArgumentException;

    UserReference save(UserReference userReference);

    UserReference findByUuid(String uuid);

    List<UserReference> findUserReferenceByEmail(String email);

    boolean delete(UserReference userReference);

    List<UserApp> getUsers();

    UserApp getUserById(Long id);

    UserApp getUserByEmail(String email);

    UserApp updateUserInfo(UserInfoDTO userInfoDTO, UserApp userApp);

    UserApp updateUserInfo(Long userId, UserInfoDTO userInfoDTO) throws Exception;

    List<RoleApp> getRoles(Long userId);

    Map<String, Object> signIn(String email, String password) throws Exception;
}
