package com.khac_dat.identity_service.controller;

import com.khac_dat.identity_service.dto.request.ApiResponse;
import com.khac_dat.identity_service.dto.request.MemberCreationRequest;
import com.khac_dat.identity_service.dto.request.MemberUpdateRequest;
import com.khac_dat.identity_service.dto.response.MemberResponse;
import com.khac_dat.identity_service.service.MemberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/members")
public class MemberController {

    MemberService memberService;

    @PostMapping
    ApiResponse<MemberResponse> createMember(@RequestBody MemberCreationRequest request){
        return ApiResponse.<MemberResponse>builder()
                .result(memberService.createMember(request))
                .build();
    }

    @PutMapping("/{memberId}")
    ApiResponse<MemberResponse> updateMember(@PathVariable String memberId, @RequestBody MemberUpdateRequest request){
        return ApiResponse.<MemberResponse>builder()
                .result(memberService.updateMember(memberId, request))
                .message("Cập nhật thông tin thành viên thành công!")
                .build();
    }

    @GetMapping
    ApiResponse<List<MemberResponse>> getMembers(){
        return ApiResponse.<List<MemberResponse>>builder()
                .result(memberService.getMembers())
                .build();
    }

    @GetMapping("/{memberId}")
    ApiResponse<MemberResponse> getMember(@PathVariable String memberId){
        return ApiResponse.<MemberResponse>builder()
                .result(memberService.getMember(memberId))
                .build();
    }

    @DeleteMapping("/{memberId}")
    ApiResponse<Void> deleteMember(@PathVariable String memberId){
        memberService.deleteMember(memberId);
        return ApiResponse.<Void>builder()
                .message("Thành viên đã bị xóa khỏi hệ thống")
                .build();
    }
}