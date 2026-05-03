package com.khac_dat.identity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentResponse {
    String id;
    String title;
    String content;
    String status;
    String ownerName;
    String departmentName;
    boolean isPublicInDepartment;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
