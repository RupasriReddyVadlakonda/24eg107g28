package com.datavault.personal_data_vault.security;

import com.datavault.personal_data_vault.entity.User;
import com.datavault.personal_data_vault.repository.UserRepository;
import com.datavault.personal_data_vault.security.UserPrincipal;
import lombok.Generated;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService
implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));
        return UserPrincipal.from(user);
    }

    @Generated
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

