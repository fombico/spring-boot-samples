package com.fombico.sameauthandresourceserver.account;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AccountUserServiceTest {

    @Mock
    AccountRepository accountRepository;

    AccountUserService accountUserService;

    @Before
    public void beforeEach() {
        accountUserService = new AccountUserService(accountRepository, new BCryptPasswordEncoder());
    }

    @Test
    public void loadUserByUsername_returnsUser_ifPresent() {
        when(accountRepository.findByUsername("joe")).thenReturn(Optional.of(
                AccountRecord.builder()
                        .username("joe")
                        .password("password")
                        .scopes("scope1 scope2")
                        .build()
        ));

        UserDetails userDetails = accountUserService.loadUserByUsername("joe");
        assertThat(userDetails.getUsername()).isEqualTo("joe");
        assertThat(userDetails.getPassword()).isEqualTo("password");
        assertThat(userDetails.getAuthorities()).isEqualToComparingFieldByFieldRecursively(
                Arrays.asList(new SimpleGrantedAuthority("scope1"), new SimpleGrantedAuthority("scope2")));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_throwsUsernameNotFoundException_whenUserNotFound() {
        accountUserService.loadUserByUsername("joe");
    }
}