package com.document.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "owner")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Owner implements Serializable {

//    @Id
//    @GeneratedValue(strategy = IDENTITY)

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "owner_id_generator")
    @SequenceGenerator(
            name = "owner_id_generator",
            sequenceName = "owner_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "owner_code")
    private String ownerCode;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;
    private Integer type;

    @Column(name = "created_stamp")
    private Date createdStamp;
}
