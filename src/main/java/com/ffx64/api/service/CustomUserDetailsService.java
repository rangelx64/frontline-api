package com.ffx64.api.service;

import com.ffx64.api.exception.UserNotFoundException;
import com.ffx64.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        UserDetails user = userRepository.findByEmailDetails(email);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
