package com.khac_dat.identity_service.dto.response;

import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    @Id
    String id;

    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
}
