package com.khac_dat.identity_service.entity;

import com.khac_dat.identity_service.enums.AuditAction;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String actorId;

    @Enumerated(EnumType.STRING)
    AuditAction action;

    String resourceType;
    String resourceId;
    String description;
    LocalDateTime createdAt;
    String ipAddress;
}