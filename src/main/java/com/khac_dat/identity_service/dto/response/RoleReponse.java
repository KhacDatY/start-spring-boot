package com.khac_dat.identity_service.dto.response;

import com.khac_dat.identity_service.entity.Permission;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleReponse {
    String id;
    String name;
    Set<PermissionReponse> permissions;
}
