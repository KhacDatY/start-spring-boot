package com.khac_dat.identity_service.mapper;

import com.khac_dat.identity_service.dto.request.DepartmentRequest;
import com.khac_dat.identity_service.dto.response.DepartmentResponse;
import com.khac_dat.identity_service.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toDepartment(DepartmentRequest request);
    DepartmentResponse toDepartmentResponse(Department department);
    void updateDepartment(@MappingTarget Department department, DepartmentRequest request);
}
