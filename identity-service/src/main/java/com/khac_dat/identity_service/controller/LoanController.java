package com.khac_dat.identity_service.controller;

import com.khac_dat.identity_service.dto.request.ApiResponse;
import com.khac_dat.identity_service.dto.request.LoanCreationRequest;
import com.khac_dat.identity_service.dto.response.BookStatisticResponse;
import com.khac_dat.identity_service.dto.response.LoanResponse;
import com.khac_dat.identity_service.dto.response.MemberStatisticResponse;
import com.khac_dat.identity_service.service.LoanService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoanController {

    LoanService loanService;

    @PostMapping
    public ApiResponse<LoanResponse> createLoan(@RequestBody LoanCreationRequest request) {
        return ApiResponse.<LoanResponse>builder()
                .result(loanService.createLoan(request))
                .message("Tạo phiếu mượn sách thành công!")
                .build();
    }

    @PostMapping("/{loanId}/return")
    public ApiResponse<LoanResponse> returnBook(@PathVariable Integer loanId) {
        return ApiResponse.<LoanResponse>builder()
                .result(loanService.returnBook(loanId))
                .message("Trả sách thành công!")
                .build();
    }

    @GetMapping
    public ApiResponse<List<LoanResponse>> getAllLoans() {
        return ApiResponse.<List<LoanResponse>>builder()
                .result(loanService.getAllLoans())
                .build();
    }

    @GetMapping("/{loanId}")
    public ApiResponse<LoanResponse> getLoan(@PathVariable Integer loanId) {
        return ApiResponse.<LoanResponse>builder()
                .result(loanService.getLoanById(loanId))
                .build();
    }

    @GetMapping("/statistics/top-books")
    public ApiResponse<List<BookStatisticResponse>> getTopBooks() {
        return ApiResponse.<List<BookStatisticResponse>>builder()
                .result(loanService.getTopBooks())
                .build();
    }

    @GetMapping("/statistics/top-members")
    public ApiResponse<List<MemberStatisticResponse>> getTopMembers() {
        return ApiResponse.<List<MemberStatisticResponse>>builder()
                .result(loanService.getTopMembers())
                .build();
    }
}