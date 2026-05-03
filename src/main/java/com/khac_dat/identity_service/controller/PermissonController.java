package com.khac_dat.identity_service.controller;

import com.khac_dat.identity_service.dto.request.ApiResponse;
import com.khac_dat.identity_service.dto.request.PermissionCreationRequest;
import com.khac_dat.identity_service.dto.response.PermissionReponse;
import com.khac_dat.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/permissions")
@Slf4j
public class PermissonController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionReponse> create(@RequestBody PermissionCreationRequest request){
        return ApiResponse.<PermissionReponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionReponse>> getAll(){
        return ApiResponse.<List<PermissionReponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permissionId}")
    ApiResponse<Void> delete(@PathVariable String permissionId){
        permissionService.delete(permissionId);
        return ApiResponse.<Void>builder().build();
    }
}
