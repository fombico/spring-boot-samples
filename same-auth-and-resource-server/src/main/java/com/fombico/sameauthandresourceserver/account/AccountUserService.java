package com.fombico.sameauthandresourceserver.account;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class AccountUserService implements UserDetailsService {

    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void insertUsers() {
        accountRepository.save(AccountRecord.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .scopes("admin user")
                .build());
        accountRepository.save(AccountRecord.builder()
                .username("fred")
                .password(passwordEncoder.encode("fred"))
                .scopes("user")
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username)
                .map(account -> User.builder()
                        .username(account.getUsername())
                        .password(account.getPassword())
                        .authorities(account.getScopes().split("\\s+"))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
