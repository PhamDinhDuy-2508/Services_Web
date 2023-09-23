package com.Search_Thesis.Search_Thesis.Security;

import com.Search_Thesis.Search_Thesis.Model.Role;
import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.repository.User_respository;
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

@Service("UserServices_Sercurity")
public class UserServices_Sercurity implements UserDetailsService {

    private User_respository user_respository;

    @Autowired
    private void setUser_respository(User_respository user_respository) {
        this.user_respository = user_respository;
    }

    private User user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user = user_respository.findUsersByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomerDetails(user);
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for(Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }

    public User getUser() {
        return user;
    }

    public UserDetails loadUserById(int id) {
        User user = user_respository.findById(id);
        return new org.springframework.security.core.userdetails.User(
                user.getAccount(), user.getPassword(), getAuthorities(user));
    }

}
