package com.angular.tekflix_server.security;

import com.angular.tekflix_server.models.User;
import com.angular.tekflix_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private Long id;
    private Collection<? extends GrantedAuthority> authorities;


    @Autowired
    public CustomUserDetailsService(UserRepository userRepository,
                                    Collection<? extends GrantedAuthority> authorities) {
        this.userRepository = userRepository;
        this.authorities = authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + name));

        // Créer une liste des rôles de l'utilisateur
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        // Retourner les détails de l'utilisateur
        return new org.springframework.security.core.userdetails.User(
                user.getName(), user.getPassword(), authorities);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
