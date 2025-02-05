package com.ekub.keycloak;

import lombok.Builder;

@Builder
public record KeycloakUserRequest(
        String username,
        String password,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        boolean enabled
) {
}
