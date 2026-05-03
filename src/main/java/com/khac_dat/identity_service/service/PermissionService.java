package com.khac_dat.identity_service.service;

import com.khac_dat.identity_service.dto.request.PermissionCreationRequest;
import com.khac_dat.identity_service.dto.response.PermissionReponse;
import com.khac_dat.identity_service.entity.Permission;
import com.khac_dat.identity_service.exception.AppException;
import com.khac_dat.identity_service.exception.ErrorCode;
import com.khac_dat.identity_service.mapper.PermissionMapper;
import com.khac_dat.identity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionReponse create(PermissionCreationRequest request){
        if (permissionRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED); // Nhớ tạo thêm ErrorCode này
        }
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissonReponse(permission);
    }

    public List<PermissionReponse> getAll(){
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissonReponse).toList();
    }
    public void delete(String permissionId){
        permissionRepository.deleteById(permissionId);
    }
}
