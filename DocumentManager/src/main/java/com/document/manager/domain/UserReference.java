package com.document.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "user_reference")
public class UserReference implements Serializable {

//    @Id
//    @GeneratedValue(strategy = IDENTITY)

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_reference_id_generator")
    @SequenceGenerator(
            name = "user_reference_id_generator",
            sequenceName = "user_reference_id_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserApp userApp;

    private String uuid;

    @Column(name = "created_stamp")
    private Date createdStamp;

    @Column(name = "expired_stamp")
    private Date expiredStamp;
}
