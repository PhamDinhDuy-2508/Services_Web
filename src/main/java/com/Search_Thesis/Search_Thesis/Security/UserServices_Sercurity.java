package com.Search_Thesis.Search_Thesis.Security;

import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.resposity.User_respository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServices_Sercurity implements UserDetailsService {

    @Autowired
    User_respository user_respository ;
    private  User user ;


    @Override


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = user_respository.findUsersByAccount(username) ;
        this.user =  user ;

        System.out.println("User test "  + user.toString());
        if(user == null) {
            throw new UsernameNotFoundException(username);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder() ;
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        String encoded =new BCryptPasswordEncoder().encode(user.getPassword());

        User user1 = new User() ;
        user1.setUser_id(user.getUser_id());
        user1.setPassword( user.getPassword());
        user1.setAccount(user.getAccount()) ;
        System.out.println(user1);
        return new CustomerDetails(user) ;

    }
    private List<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        // System.out.print("authorities :"+authorities);
        return authorities;
    }

    public User getUser() {
        return user;
    }
//
//
//        return new CustomerDetails(user);
//    }
    @Transactional
    public UserDetails loadUserById(int id) {
        User user = user_respository.findById(id);
        System.out.println(" test User"  + user);

        return new org.springframework.security.core.userdetails.User(
                user.getAccount() , user.getPassword() ,getAuthorities(user));
    }

}
