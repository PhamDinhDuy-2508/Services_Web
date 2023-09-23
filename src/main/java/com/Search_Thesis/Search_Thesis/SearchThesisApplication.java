package com.Search_Thesis.Search_Thesis;

import com.Search_Thesis.Search_Thesis.Model.Question;
import com.Search_Thesis.Search_Thesis.repository.Question_Repository;
import com.Search_Thesis.Search_Thesis.repository.Tag_repository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

//@SpringBootApplication(scanBasePackages = {"com.Search_Thesis.Search_Thesis.resposity" ,
//		"com.Search_Thesis.Search_Thesis.Services" ,"com.Search_Thesis.Search_Thesis.Model" ,
//		"com.Search_Thesis.Search_Thesis.Security","com.Search_Thesis.Search_Thesis.JWT",
//		"com.Search_Thesis.Search_Thesis.SortBy" ,"com.Search_Thesis.Search_Thesis.Rest","com.Search_Thesis.Search_Thesis.Controller"  } ,
//scanBasePackageClasses = {SignIn_Respository.class})

@SpringBootApplication
@EnableCaching

@RequiredArgsConstructor
//@EnableJpaRepositories(basePackages = {"com.Search_Thesis.Search_Thesis.resposity" ,
//		"com.Search_Thesis.Search_Thesis.Services"}  ,
//		basePackageClasses = SignIn_Respository.class  )


public class SearchThesisApplication{


	public static void main(String[] args) {
		SpringApplication.run(SearchThesisApplication.class, args);
	}
}
