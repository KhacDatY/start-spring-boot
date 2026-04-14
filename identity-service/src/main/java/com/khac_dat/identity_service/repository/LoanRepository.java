package com.khac_dat.identity_service.repository;

import com.khac_dat.identity_service.entity.Book;
import com.khac_dat.identity_service.entity.Loan;
import com.khac_dat.identity_service.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {

    List<Loan> findAllByMemberId(String memberId);

    Optional<Loan> findByBookIdAndReturnDateIsNull(String bookId);

    boolean existsByBookIdAndReturnDateIsNull(String bookId);

    List<Loan> findAllByMemberIdAndReturnDateIsNull(String memberId);
    @Query("SELECT l.book, COUNT(l.id) as count FROM Loan l " +
            "GROUP BY l.book ORDER BY count DESC")
    List<Object[]> findTopBooks();

    // Thống kê độc giả: Đếm số lần member_id xuất hiện trong bảng Loan
    @Query("SELECT l.member, COUNT(l.id) as count FROM Loan l " +
            "GROUP BY l.member ORDER BY count DESC")
    List<Object[]> findTopMembers();

}