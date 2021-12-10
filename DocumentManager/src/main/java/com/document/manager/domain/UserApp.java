package com.document.manager.domain;

import com.document.manager.dto.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.annotations.CascadeType.*;

@Entity
@Table(name = "user_app")
@Data
@AllArgsConstructor
public class UserApp implements Serializable {

//    @Id
//    @GeneratedValue(strategy = IDENTITY)
//    @Column(name = "id")
//    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_app_id_generator")
    @SequenceGenerator(
            name = "user_app_id_generator",
            sequenceName = "user_app_id_seq",
            allocationSize = 1
    )
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
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_app_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Cascade(ALL)
    private Collection<RoleApp> roleApps = new ArrayList<>();

//    @OneToMany(mappedBy = "user")
//    private Set<Report> reports;
    
    public UserApp() {
        isActive = true;
        createdStamp = new Timestamp(System.currentTimeMillis());
    }
}
