package com.ekub.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record UserRequest(
        String id,
        @NotNull(message = "username is mandatory")
        @NotEmpty(message = "Username is mandatory")
        String username,
        //@NotEmpty(message = "Password is mandatory")
        String password,
        @NotEmpty(message = "FirstName is mandatory")
        String firstName,
        @NotEmpty(message = "LastName is mandatory")
        String lastName,
        @Email(message = "Email format not accepted")
        String email,
        @NotNull(message = "PhoneNumber is mandatory")
        @NotBlank(message = "PhoneNumber is mandatory")
        @NotEmpty(message = "PhoneNumber is mandatory")
        String phoneNumber,
        @NotEmpty(message = "Profession is mandatory")
        String profession,
        boolean enabled,
        List<String> ekubIds,
        String remark

) {}
