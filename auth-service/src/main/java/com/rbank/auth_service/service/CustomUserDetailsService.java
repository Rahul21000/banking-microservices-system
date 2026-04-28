package com.rbank.auth_service.service;

import com.rbank.auth_service.client.UserServiceClient;
import com.rbank.auth_service.dto.CustomerAuthDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserServiceClient userServiceClient;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomerAuthDTO user = userServiceClient.getCustomerWithRoles(username);
        if(user == null ){
            throw new UsernameNotFoundException("user not found " + username);
        }
        return UserDetailsImpl.build(user);
    }
}
