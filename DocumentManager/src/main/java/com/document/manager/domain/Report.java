package com.document.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String title;
    private String note;
    private String fileName;
    private String content;
    private String vector;
    private String link;
    private Double mark;
    private Timestamp createdStamp;

    @ManyToMany(fetch = EAGER)
    private List<Owner> owners = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "created_id")
    private User user;
}
