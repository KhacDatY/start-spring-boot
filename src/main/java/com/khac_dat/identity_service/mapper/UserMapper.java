package com.khac_dat.identity_service.mapper;

import com.khac_dat.identity_service.dto.request.UserCreationRequest;
import com.khac_dat.identity_service.dto.request.UserUpdateRequest;
import com.khac_dat.identity_service.dto.response.UserResponse;
import com.khac_dat.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "department", ignore = true)
    User toUser(UserCreationRequest request);

    @Mapping(source = "department.name", target = "departmentName")
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}