package com.document.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String userCode;
    private String firstname;
    private String lastname;
    private String dob;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private Boolean isActive;
    private Timestamp createdStamp;
    private Timestamp modifiedStamp;

    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

//    @OneToMany(mappedBy = "user")
//    private Set<Report> reports;
}
