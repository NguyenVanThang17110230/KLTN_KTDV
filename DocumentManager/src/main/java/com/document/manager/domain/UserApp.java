package com.document.manager.domain;

import com.document.manager.dto.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "user_app")
@Data
@AllArgsConstructor
public class UserApp implements Serializable {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_code")
    private String userCode;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    private Date dob;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_stamp")
    private Date createdStamp;

    @Column(name = "modified_stamp")
    private Date modifiedStamp;

    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

//    @OneToMany(mappedBy = "user")
//    private Set<Report> reports;


    public UserApp() {
        isActive = false;
        createdStamp = new Timestamp(System.currentTimeMillis());
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        for (Role role : this.roles) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//        return authorities;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return false;
//    }
}