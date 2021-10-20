package com.document.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleApp implements Serializable {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String name;

//    @ManyToMany(mappedBy = "roles")
//    private Collection<UserApp> userApps = new ArrayList<>();
}
