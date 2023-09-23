package com.Search_Thesis.Search_Thesis.Security;

import com.Search_Thesis.Search_Thesis.Model.User;
import com.Search_Thesis.Search_Thesis.repository.User_respository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private User_respository userRepository;
    @Autowired
    User user ;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUsersByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomerDetails(user);
    }

}
