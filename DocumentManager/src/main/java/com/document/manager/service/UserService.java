package com.document.manager.service;

import com.document.manager.domain.Role;
import com.document.manager.domain.User;

public interface UserService {

    User findUserById(Long id);

    User save(User user);

    Role save (Role role);

    Role findRoleByName(String roleName);
}
