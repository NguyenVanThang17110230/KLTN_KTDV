package com.document.manager.domain;

import com.document.manager.dto.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String userCode;
    private String firstname;
    private String lastname;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    private Date dob;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (Role role : this.roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
