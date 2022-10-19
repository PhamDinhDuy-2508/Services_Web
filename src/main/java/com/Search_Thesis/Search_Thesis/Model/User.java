package com.Search_Thesis.Search_Thesis.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Component("User")
@Scope(
        value = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
@EnableAutoConfiguration
@Table(name = "User")
public class User {
    private static final long serialVersionUID = -297553281792804396L;


    @Column
    private String first_name  ;

    @Column
    private  String last_name ;

    @Column
    private  String email ;

    @Column
    private  String Phone ;

    @Column
    private  String city  ;

    @Column
    private  String State ;

    @Column
    private  String province ;

    @Column
    private  String sex ;

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private  int user_id =0;

    @Column(name = "account")
    private String account ;

    private String   password ;

    @Column(name="Home_number")
    private String Home_number ;
    @Column(name = "street")
    private String Street ;

    @Column(name = "country")
    private String country ;

    private  String role ;

    @Column
    private String resettoken= "" ;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "creator")
    @JsonIgnore
    private Set<Question> questionList ;




    @Override
    public String toString() {
        return "User{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", Phone='" + Phone + '\'' +
                ", city='" + city + '\'' +
                ", State='" + State + '\'' +
                ", province='" + province + '\'' +
                ", sex='" + sex + '\'' +
                ", user_id=" + user_id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", Home_number='" + Home_number + '\'' +
                ", Street='" + Street + '\'' +
                ", country='" + country + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
