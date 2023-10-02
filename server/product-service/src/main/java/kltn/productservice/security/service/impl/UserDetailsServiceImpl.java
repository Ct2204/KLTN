
package kltn.productservice.security.service.impl;


import kltn.productservice.security.jwt.JwtUtils;
import kltn.productservice.util.CallApiOtherService;
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
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CallApiOtherService callApiOtherService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        String token = callApiOtherService.getToken();
        email = jwtUtils.extractUserName(token);
        List<String> roles = jwtUtils.extractRolesFromToken(token);

        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String role: roles){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
        }

        return new org.springframework.security.core.userdetails.User(email,"",authorities);

    }

}
