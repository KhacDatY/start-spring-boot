package com.khac_dat.identity_service.service;

import com.khac_dat.identity_service.dto.request.DepartmentRequest;
import com.khac_dat.identity_service.dto.response.DepartmentResponse;
import com.khac_dat.identity_service.entity.Department;
import com.khac_dat.identity_service.exception.AppException;
import com.khac_dat.identity_service.exception.ErrorCode;
import com.khac_dat.identity_service.mapper.DepartmentMapper;
import com.khac_dat.identity_service.repository.DepartmentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.DEPARTMENT_EXISTED);
        }
        Department dept = departmentMapper.toDepartment(request);
        return departmentMapper.toDepartmentResponse(departmentRepository.save(dept));
    }

    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toDepartmentResponse).toList();
    }

    public DepartmentResponse update(String id, DepartmentRequest request) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        departmentMapper.updateDepartment(dept, request);
        return departmentMapper.toDepartmentResponse(departmentRepository.save(dept));
    }

    public void delete(String id) {
        if (!departmentRepository.existsById(id)) {
            throw new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED);
        }
        departmentRepository.deleteById(id);
    }
}
