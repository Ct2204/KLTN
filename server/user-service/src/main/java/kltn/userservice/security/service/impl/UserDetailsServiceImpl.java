package kltn.userservice.security.service.impl;


import kltn.userservice.security.model.User;
import kltn.userservice.security.repository.UserSecurityRepository;
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
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserSecurityRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository
                .findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with email" + email));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),mapRolesToAuthority(user));
    }

    /**
     * *
     * This method handles RoleEntity to GrantedAuthority
     * @return a list GrantedAuthority corresponding to the user's role, which can be used during authentication.
     */
    private Collection<GrantedAuthority> mapRolesToAuthority(User user) {

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream())
                .collect(Collectors.toList());
        return authorities;
    }
}
