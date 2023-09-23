package com.Search_Thesis.Search_Thesis.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;
@Entity
@Table(name = "roles")
@Component("Role")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id" )
    int roleID ;

    @Column(name = "name")
    String role ;

    @ManyToMany(mappedBy = "roles" , fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @Transient
    private Set<User> userSet ;

}
