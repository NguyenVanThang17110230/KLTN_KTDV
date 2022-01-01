package com.document.manager.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "sentences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sentences implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private DocumentApp documentApp;

    @Column(name = "raw_text", columnDefinition = "text")
    private String rawText;

    @Column(columnDefinition = "text")
    private String tokenizer;
}
