package com.khac_dat.identity_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    String email;

    @Size(min = 8, message = "PASSWORD_NVALID")
    String password;

    String firstName;
    String lastName;
    LocalDate dob;

    String departmentId;
    String role;

}
