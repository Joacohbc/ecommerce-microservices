package com.microecommerce.usersauthservice.config;

import com.microecommerce.utilitymodule.models.users.UserCredentials;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;

    public CustomUserDetails(UserCredentials userCredential) {
        this.username = userCredential.getUsername();
        this.password = userCredential.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // TODO: Implement the following methods
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // TODO: Implement the following methods
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // TODO: Implement the following methods
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // TODO: Implement the following methods
    @Override
    public boolean isEnabled() {
        return true;
    }
}
