package com.document.manager.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "document")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentApp implements Serializable {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String title;
    private String note;

    @Column(name = "file_name")
    private String fileName;
    private String link;
    private Double mark;

    @Column(name = "created_stamp")
    private Date createdStamp;

    @ManyToMany(fetch = EAGER)
    private List<Owner> owners = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "created_id")
    private UserApp userApp;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    private List<Sentences> sentences;

}
