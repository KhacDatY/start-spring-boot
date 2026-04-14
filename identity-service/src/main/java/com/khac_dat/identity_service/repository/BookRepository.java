package com.khac_dat.identity_service.repository;

import com.khac_dat.identity_service.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
}
