package com.khac_dat.identity_service.dto.response;

import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;

    String email;
    String firstName;
    String lastName;
    LocalDate dob;

    Set<RoleReponse> roles;
}
