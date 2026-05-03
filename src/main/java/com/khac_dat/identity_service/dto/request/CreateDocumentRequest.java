package com.khac_dat.identity_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDocumentRequest {
    @NotBlank
    String title;
    @NotBlank String content;
    boolean isPublicInDepartment;
}