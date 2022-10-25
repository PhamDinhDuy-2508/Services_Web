package com.Search_Thesis.Search_Thesis.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;
@Component
@Entity
@Table(name = "reply")
@EnableAutoConfiguration
@Data
public class Reply {
    @Id
    @Column( name = "idreply")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int Reply_id ;

    @Column(name = "Date_create")
    private Date Date_create ;

    @Column(name =  "Content")
    private String content ;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @JsonIgnore
    @JoinColumn(name = "Question_id"  , referencedColumnName = "id_question" ,columnDefinition = "json"
            , nullable = true)
    // LAZY để tránh việc truy xuất dữ liệu không cần thiết. Lúc nào cần thì mới query
    private Question question ;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @JoinColumn(name = "use_id"  , referencedColumnName = "user_id" ,columnDefinition = "json"
            , nullable = true)
    @JsonIgnore
    private User user ;


}
