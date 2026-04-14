package com.khac_dat.identity_service.controller;

import com.khac_dat.identity_service.dto.request.ApiResponse;
import com.khac_dat.identity_service.dto.request.BookCreationRequest;
import com.khac_dat.identity_service.dto.request.BookUpdateRequest;
import com.khac_dat.identity_service.dto.request.UserUpdateRequest;
import com.khac_dat.identity_service.dto.response.BookResponse;
import com.khac_dat.identity_service.dto.response.UserResponse;
import com.khac_dat.identity_service.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/books")
public class BookController {

    private BookService bookService;

    @PostMapping
    ApiResponse<BookResponse> createBook(@RequestBody BookCreationRequest request){
        return ApiResponse.<BookResponse>builder()
                .result(bookService.createBook(request))
                .build();
    }
    @PutMapping("/{bookId}")
    ApiResponse<BookResponse> updateBook(@PathVariable String bookId, @RequestBody BookUpdateRequest request){
        return ApiResponse.<BookResponse>builder()
                .result(bookService.updateBook(bookId , request))
                .message("Cập nhật thông tin sách!")
                .build();
    }
    @GetMapping
    ApiResponse<List<BookResponse>> getBooks(){
        return ApiResponse.<List<BookResponse>>builder()
                .result(bookService.getBooks())
                .build();
    }

    @GetMapping("/{bookId}")
    ApiResponse<BookResponse> getBook(@PathVariable String bookId){
        return ApiResponse.<BookResponse>builder()
                .result(bookService.getBook(bookId))
                .build();
    }
    @DeleteMapping("/{bookId}")
    ApiResponse deleteBook(@PathVariable String bookId){
        bookService.deleteBook(bookId);
        return ApiResponse.builder()
                .message("Sach da bi xoa")
                .build();
    }
}
