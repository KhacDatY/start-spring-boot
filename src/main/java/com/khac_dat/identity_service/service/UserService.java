package com.khac_dat.identity_service.service;

import com.khac_dat.identity_service.dto.request.UserCreationRequest;
import com.khac_dat.identity_service.dto.request.UserUpdateRequest;
import com.khac_dat.identity_service.dto.response.UserResponse;
import com.khac_dat.identity_service.entity.Role;
import com.khac_dat.identity_service.entity.User;
import com.khac_dat.identity_service.enums.RoleName;
import com.khac_dat.identity_service.exception.AppException;
import com.khac_dat.identity_service.exception.ErrorCode;
import com.khac_dat.identity_service.mapper.UserMapper;
import com.khac_dat.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

import com.khac_dat.identity_service.repository.RoleRepository;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request){

        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);

        var userRole = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        var roleUser = roleRepository.findByName(RoleName.EMPLOYEE.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleUser);
        roles.add(userRole);
        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }


    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers(){
        log.info("dang trong method");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    public UserResponse getMyInfo(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse getUser(String userId){
        log.info("Trong method lay user bằng userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }
    
}
