package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.resposity.SignIn_Respository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class customerDetailsServices implements UserDetailsService {
    private   SignIn_Respository _signIn_respository ;
    @Autowired
    User user_;
    public customerDetailsServices(SignIn_Respository _signIn_respository) {
        this._signIn_respository = _signIn_respository;
    }
    public customerDetailsServices() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {


            this.user_ =  _signIn_respository.findUsersByAccount(username);

            List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();

            GrantedAuthority authority = new SimpleGrantedAuthority("USER");

            String noop_pass  = "{noop}"+this.user_.getPassword() ;

            grantList.add(authority);
            System.out.println(authority.toString());


            return new org.springframework.security.core.userdetails.User(username, noop_pass,
                  grantList );
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return  null ;
        }


    }
    public User load_data_base(){
        this.user_ =  _signIn_respository.findUsersByAccount("duy");
        return this.user_ ;

    }
    public User getUser_() {
        return user_;
    }
    public String get_Role(){
        return this.user_.getRole() ;
    }
    public void print(){
        System.out.println("CUSTOMER");
    }
}
