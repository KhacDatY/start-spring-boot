package com.khac_dat.identity_service.mapper;

import com.khac_dat.identity_service.dto.request.BookCreationRequest;
import com.khac_dat.identity_service.dto.request.BookUpdateRequest;
import com.khac_dat.identity_service.dto.response.BookResponse;
import com.khac_dat.identity_service.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toBook(BookCreationRequest request);
    BookResponse toBookResponse(Book book);
    void updateBook(@MappingTarget Book Book, BookUpdateRequest request);
}
