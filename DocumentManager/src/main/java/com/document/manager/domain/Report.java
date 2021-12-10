package com.document.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report implements Serializable {

//    @Id
//    @GeneratedValue(strategy = IDENTITY)

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_id_generator")
    @SequenceGenerator(
            name = "report_id_generator",
            sequenceName = "report_id_seq",
            allocationSize = 1
    )
    private Long id;

    private String title;
    private String note;

    @Column(name = "file_name")
    private String fileName;

    private String content;
    private String vector;
    private String link;
    private Double mark;

    @Column(name = "created_stamp")
    private Date createdStamp;

    @ManyToMany(fetch = EAGER)
    private List<Owner> owners = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "created_id")
    private UserApp userApp;
}
