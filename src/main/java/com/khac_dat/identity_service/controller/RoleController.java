package com.khac_dat.identity_service.controller;

import com.khac_dat.identity_service.dto.request.ApiResponse;
import com.khac_dat.identity_service.dto.request.RoleCreationRequest;
import com.khac_dat.identity_service.dto.response.PermissionReponse;
import com.khac_dat.identity_service.dto.response.RoleReponse;
import com.khac_dat.identity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/roles")
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleReponse> create(@RequestBody RoleCreationRequest request){
        return ApiResponse.<RoleReponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleReponse>> getAll(){
        return ApiResponse.<List<RoleReponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{roleId}")
    ApiResponse<Void> delete(@PathVariable String roleId){
        roleService.delete(roleId);
        return ApiResponse.<Void>builder().build();
    }
}
