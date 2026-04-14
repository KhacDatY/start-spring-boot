package com.khac_dat.identity_service.service;

import com.khac_dat.identity_service.dto.request.BookCreationRequest;
import com.khac_dat.identity_service.dto.request.BookUpdateRequest;
import com.khac_dat.identity_service.dto.response.BookResponse;
import com.khac_dat.identity_service.dto.response.UserResponse;
import com.khac_dat.identity_service.entity.Book;
import com.khac_dat.identity_service.exception.AppException;
import com.khac_dat.identity_service.exception.ErrorCode;
import com.khac_dat.identity_service.mapper.BookMapper;
import com.khac_dat.identity_service.repository.BookRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {
    BookRepository bookRepository;
    BookMapper bookMapper;

    public BookResponse createBook(BookCreationRequest request){
        if(bookRepository.existsById(request.getId())){
            throw new AppException(ErrorCode.BOOK_EXISTED);
        }
        return bookMapper.toBookResponse(bookRepository.save(bookMapper.toBook(request)));
    }
    public BookResponse updateBook(String bookId, BookUpdateRequest request){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_EXISTED));
        bookMapper.updateBook(book,request);
        return bookMapper.toBookResponse(bookRepository.save(book));
    }
    public List<BookResponse> getBooks(){
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookResponse).toList();
    }
    public BookResponse getBook(String bookId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new AppException(ErrorCode.BOOK_NOT_EXISTED));
        return bookMapper.toBookResponse(book);
    }
    public void deleteBook(String bookId){
        bookRepository.deleteById(bookId);
    }
}
