package com.Search_Thesis.Search_Thesis;

import com.Search_Thesis.Search_Thesis.resposity.User_respository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;

//@SpringBootApplication(scanBasePackages = {"com.Search_Thesis.Search_Thesis.resposity" ,
//		"com.Search_Thesis.Search_Thesis.Services" ,"com.Search_Thesis.Search_Thesis.Model" ,
//		"com.Search_Thesis.Search_Thesis.Security","com.Search_Thesis.Search_Thesis.JWT",
//		"com.Search_Thesis.Search_Thesis.Filter" ,"com.Search_Thesis.Search_Thesis.Rest","com.Search_Thesis.Search_Thesis.Controller"  } ,
//scanBasePackageClasses = {SignIn_Respository.class})

@SpringBootApplication
@EnableCaching

@RequiredArgsConstructor
//@EnableJpaRepositories(basePackages = {"com.Search_Thesis.Search_Thesis.resposity" ,
//		"com.Search_Thesis.Search_Thesis.Services"}  ,
//		basePackageClasses = SignIn_Respository.class  )


public class SearchThesisApplication implements CommandLineRunner {

	@Autowired
	User_respository user_respository ;
	@Autowired
	PasswordEncoder passwordEncoder ;

	public static void main(String[] args) {
		SpringApplication.run(SearchThesisApplication.class, args);
	}
//	private  final Root_Responsitory root_responsitory ;
	@Override
	public void run(String... args) throws Exception {
//		User user =  new User() ;
//		user.setAccount("PhamDinhDuy2508");
//		user.setPassword(passwordEncoder.encode("25082000"));
//		user_respository.save(user) ;


//		Root_Folder root_folder =  new Root_Folder() ;
//		root_folder.setId(2);
//		root_folder.setName("Chuyên Ngành");
//
//		Category_document category_document =  new Category_document() ;
//		category_document.setName("CAD Ứng Dụng");
//		category_document.setCode("AS2013");
//		category_document.setRoot_folder(root_folder);
//
//		root_folder.setCategory_document(Collections.singleton(category_document));
//		root_responsitory.save(root_folder) ;
//
//
//
//
//		categoryDocument_responsitory.findAll().forEach(p -> {
//			System.out.println(p.getName());
//		});
	}

}
