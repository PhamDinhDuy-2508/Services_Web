package com.Search_Thesis.Search_Thesis.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
@Component
@Entity
@Table(name = "reply")
@EnableAutoConfiguration



public class Reply {
    @Id
    @Column( name = "idreply")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int Reply_id ;

    @Column(name = "Date_create")
    private Date Date_create ;

    @Column(name =  "Content")
    private String content ;

    @ManyToMany(mappedBy = "reply")
    // LAZY để tránh việc truy xuất dữ liệu không cần thiết. Lúc nào cần thì mới query
    private Collection<Question> questionCollection;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @JsonIgnore

    private User user ;
}
