package com.gk.SpringSecurityJPA.SpringSecurityJPA.Service;

import com.gk.SpringSecurityJPA.SpringSecurityJPA.DAO.UserRepository;
import com.gk.SpringSecurityJPA.SpringSecurityJPA.Entity.MyUserDetails;
import com.gk.SpringSecurityJPA.SpringSecurityJPA.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Now we fetch the users from the db using the spring jpa repository
        Optional<User> user = userRepository.findByUserName(username);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found" + username));

        return user.map(usr->new MyUserDetails(usr)).get();
    }
}
