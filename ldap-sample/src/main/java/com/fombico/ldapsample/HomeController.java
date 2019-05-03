package com.fombico.ldapsample;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index(Authentication authentication) {
        LdapUserDetails ldapUserDetails = (LdapUserDetails) authentication.getPrincipal();
        return "User " + authentication.getName() + " with DN: " + ldapUserDetails.getDn() + " has the following authorities: " + authentication.getAuthorities();
    }

    @PreAuthorize("hasRole('ROLE_DEVELOPERS')")
    @GetMapping("/dev")
    public String dev() {
        return "Hello, developer!";
    }

    @PreAuthorize("hasRole('ROLE_MANAGERS')")
    @GetMapping("/manager")
    public String manager() {
        return "Hello, manager!";
    }
}
