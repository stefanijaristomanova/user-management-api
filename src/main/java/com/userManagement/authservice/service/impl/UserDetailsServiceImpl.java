package com.userManagement.authservice.service.impl;

 import com.userManagement.authservice.persistence.entity.UserEntity;
import com.userManagement.authservice.persistence.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
 import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        return new User(userEntity.getEmail(), userEntity.getPassword(), grantedAuthorities);
    }
}
