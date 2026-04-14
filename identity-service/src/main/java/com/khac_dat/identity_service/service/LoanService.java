package com.khac_dat.identity_service.service;

import com.khac_dat.identity_service.dto.request.LoanCreationRequest;
import com.khac_dat.identity_service.dto.response.BookStatisticResponse;
import com.khac_dat.identity_service.dto.response.LoanResponse;
import com.khac_dat.identity_service.dto.response.MemberStatisticResponse;
import com.khac_dat.identity_service.entity.Book;
import com.khac_dat.identity_service.entity.Loan;
import com.khac_dat.identity_service.entity.Member;
import com.khac_dat.identity_service.exception.AppException;
import com.khac_dat.identity_service.exception.ErrorCode;
import com.khac_dat.identity_service.mapper.BookMapper;
import com.khac_dat.identity_service.mapper.LoanMapper;
import com.khac_dat.identity_service.mapper.MemberMapper;
import com.khac_dat.identity_service.repository.BookRepository;
import com.khac_dat.identity_service.repository.LoanRepository;
import com.khac_dat.identity_service.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoanService {
    LoanRepository loanRepository;
    BookRepository bookRepository;
    MemberRepository memberRepository;
    LoanMapper loanMapper;
    BookMapper bookMapper;
    MemberMapper memberMapper;

    @Transactional
    public LoanResponse createLoan(LoanCreationRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_EXISTED));

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_EXISTED));

        if (loanRepository.existsByBookIdAndReturnDateIsNull(request.getBookId())) {
            throw new AppException(ErrorCode.BOOK_ALREADY_BORROWED);
        }

        Loan loan = loanMapper.toLoan(request);
        loan.setBook(book);
        loan.setMember(member);

        return loanMapper.toLoanResponse(loanRepository.save(loan));
    }

    @Transactional
    public LoanResponse returnBook(Integer loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new AppException(ErrorCode.LOAN_NOT_FOUND));

        if (loan.getReturnDate() != null) {
            throw new AppException(ErrorCode.BOOK_ALREADY_RETURNED);
        }
        loan.setReturnDate(LocalDate.now());

        return loanMapper.toLoanResponse(loanRepository.save(loan));
    }

    public List<LoanResponse> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(loanMapper::toLoanResponse)
                .toList();
    }

    public LoanResponse getLoanById(Integer loanId) {
        return loanRepository.findById(loanId)
                .map(loanMapper::toLoanResponse)
                .orElseThrow(() -> new AppException(ErrorCode.LOAN_NOT_FOUND));
    }

    public List<BookStatisticResponse> getTopBooks() {
        return loanRepository.findTopBooks().stream()
                .map(result -> new BookStatisticResponse(
                        bookMapper.toBookResponse((Book) result[0]),
                        (Long) result[1]
                )).toList();
    }

    public List<MemberStatisticResponse> getTopMembers() {
        return loanRepository.findTopMembers().stream()
                .map(result -> new MemberStatisticResponse(
                        memberMapper.toMemberResponse((Member) result[0]),
                        (Long) result[1]
                )).toList();
    }
}