package com.rbank.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbank.user_service.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(unique = true,nullable = false)
    private RoleType roleType;
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<Customer> customers;


}


