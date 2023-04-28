package com.app.utils;


import com.app.entities.User;
import com.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JwtUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    user.get().getEmail(),
                    user.get().getPassword(),
                    authorities(user.get())
            );
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    private List<SimpleGrantedAuthority> authorities(User user) {
        List<SimpleGrantedAuthority> auths = new ArrayList<>();
        user.getRoles().forEach(role -> {
            auths.add(new SimpleGrantedAuthority( "ROLE_"+ role.getName()));
        });

        return auths;
    }
}
