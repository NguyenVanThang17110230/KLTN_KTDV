package com.document.manager.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;

@Entity
@Table(name = "users_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersRoles {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name ="user_app_id")
    private Long userAppId;

    @Column(name ="role_id")
    private Long roleId;
}
