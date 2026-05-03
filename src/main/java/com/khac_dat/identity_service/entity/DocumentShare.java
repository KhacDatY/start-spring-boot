package com.khac_dat.identity_service.entity;

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
public class DocumentShare {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_with_user_id")
    User sharedWithUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_with_department_id")
    Department sharedWithDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_by_id")
    User sharedBy;

    LocalDateTime createdAt;
}