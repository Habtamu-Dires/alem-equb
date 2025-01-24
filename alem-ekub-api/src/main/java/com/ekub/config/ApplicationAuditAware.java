package com.ekub.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null &&
            authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof Jwt jwt){
            return Optional.ofNullable(jwt.getClaim("preferred_username"));
        }  else {
            return Optional.empty();
        }
    }
}
