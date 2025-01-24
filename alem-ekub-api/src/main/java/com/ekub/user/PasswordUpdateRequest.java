package com.ekub.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record PasswordUpdateRequest(
        @NotNull(message = "Username is mandatory")
        @NotEmpty(message = "Username is mandatory")
        String username,
        @NotNull(message = "old password is mandatory")
        @NotEmpty(message = "old password is mandatory")
        String oldPassword,
        @NotNull(message = "new password is mandatory")
        @NotEmpty(message = "new password is mandatory")
        String newPassword
) {}
