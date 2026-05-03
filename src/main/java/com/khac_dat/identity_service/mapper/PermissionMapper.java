package com.khac_dat.identity_service.mapper;
import com.khac_dat.identity_service.dto.request.PermissionCreationRequest;
import com.khac_dat.identity_service.dto.response.PermissionReponse;
import com.khac_dat.identity_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreationRequest request);

    PermissionReponse toPermissonReponse(Permission permission);
}
