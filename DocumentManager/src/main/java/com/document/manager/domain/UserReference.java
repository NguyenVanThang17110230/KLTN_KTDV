package com.document.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "user_reference")
public class UserReference {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String uuid;

    @Column(name = "created_stamp")
    private Date createdStamp;

    @Column(name = "expired_stamp")
    private Date expiredStamp;
}
