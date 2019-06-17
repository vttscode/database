package com.codeacademy.hibernatetutorial.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "employee")
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String position;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "address")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "company")
    private Company company;


    @Setter
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<Contact> contacts;

    public Set<Contact> getContacts() {
        return contacts == null ? new HashSet<>() : contacts;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", address=" + address +
                ", company=" + company +
                ", contacts=" + contacts +
                '}';
    }
}
