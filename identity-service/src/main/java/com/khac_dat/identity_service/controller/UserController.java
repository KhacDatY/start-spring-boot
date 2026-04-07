package com.khac_dat.identity_service.controller;

import com.khac_dat.identity_service.dto.request.ApiResponse;
import com.khac_dat.identity_service.dto.request.UserCreationRequest;
import com.khac_dat.identity_service.dto.request.UserUpdateRequest;
import com.khac_dat.identity_service.dto.response.UserResponse;
import com.khac_dat.identity_service.entity.User;
import com.khac_dat.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @PostMapping
    ApiResponse createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<User> apiResponse= new ApiResponse<>();

        apiResponse.setResult(userService.createUser(request));

        return apiResponse;
    }

    @GetMapping
    ApiResponse getUsers() {
        ApiResponse<List<User>> apiResponse= new ApiResponse<>();

        apiResponse.setResult(userService.getUsers());

        return apiResponse;
    }

    @GetMapping("/{userId}")
    ApiResponse getUser(@PathVariable("userId") String userId){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUser(userId));
        return apiResponse;
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request){

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        UserResponse updateUser = userService.updateUser(userId , request);
        apiResponse.setMessage("Cập nhật thông tin người dùng thành công!");
        apiResponse.setResult(updateUser);
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    ApiResponse deleteUser(@PathVariable String userId){
        ApiResponse apiResponse= new ApiResponse<>();
        userService.deleteUser(userId);
        apiResponse.setMessage("Người dùng đã bị xoá");
        return apiResponse;
    }

}
