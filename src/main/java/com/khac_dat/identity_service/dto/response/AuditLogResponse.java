package com.khac_dat.identity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditLogResponse {
    String id;
    String actorId;
    String action;
    String resourceType;
    String resourceId;
    String description;
    LocalDateTime createdAt;
    String ipAddress;
}