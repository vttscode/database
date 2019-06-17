package com.codeacademy.hibernatetutorial.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Company {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address")
    private Address address;

    @Setter
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<Branch> branches;

    public Set<Branch> getBranches() {
        return branches == null ? new HashSet<>() : branches;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\": " + id +
                ", \"name\": \"" + name +
                "\", \"address\": " + (address != null ? address.getId() : "null") +
                ", \"branches\": \"" + branches +
                "\"}";
    }
}
