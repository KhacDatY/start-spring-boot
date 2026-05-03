package com.khac_dat.identity_service.mapper;


import com.khac_dat.identity_service.dto.request.RoleCreationRequest;
import com.khac_dat.identity_service.dto.response.RoleReponse;
import com.khac_dat.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest request);
    RoleReponse toRoleResponse(Role role);
}
