package com.khac_dat.identity_service.service;

import com.khac_dat.identity_service.dto.request.RoleCreationRequest;
import com.khac_dat.identity_service.dto.response.RoleReponse;
import com.khac_dat.identity_service.exception.AppException;
import com.khac_dat.identity_service.exception.ErrorCode;
import com.khac_dat.identity_service.mapper.RoleMapper;
import com.khac_dat.identity_service.repository.PermissionRepository;
import com.khac_dat.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    public RoleReponse create(RoleCreationRequest request){
        // 1. Kiểm tra xem Role Name đã tồn tại chưa
        if (roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED); // Nhớ tạo thêm ErrorCode này
        }

        var role = roleMapper.toRole(request);

        var permissions = request.getPermission().stream()
                .map(permissionName -> permissionRepository.findByName(permissionName)
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED)))
                .collect(Collectors.toSet());

        role.setPermissions(permissions);
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleReponse> getAll(){
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }
    public void delete(String roleId){
        roleRepository.deleteById(roleId);
    }
}
