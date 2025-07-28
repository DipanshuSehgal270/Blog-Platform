package com.blog.platform.blog.platform.security.auth;

import com.blog.platform.blog.platform.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.blog.platform.blog.platform.entity.User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("user not found with username "+username));

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getUserrole().name());

        return new User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
}
