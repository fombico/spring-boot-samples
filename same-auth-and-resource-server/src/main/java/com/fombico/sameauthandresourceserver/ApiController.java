package com.fombico.sameauthandresourceserver;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/api/info")
    public Authentication info(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/api/user")
    @PreAuthorize("hasAuthority('user')")
    public String user() {
        return "has user authority";
    }

    @GetMapping("/api/scope/user")
    @PreAuthorize("#oauth2.hasScope('user')")
    public String userScope() {
        return "has user scope";
    }

    @GetMapping("/api/admin")
    @PreAuthorize("hasAuthority('admin')")
    public String admin() {
        return "has admin authority";
    }

    @GetMapping("/api/scope/admin")
    @PreAuthorize("#oauth2.hasScope('admin')")
    public String adminScope() {
        return "has admin scope";
    }
}
