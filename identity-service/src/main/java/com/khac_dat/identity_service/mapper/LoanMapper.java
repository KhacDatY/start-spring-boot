package com.khac_dat.identity_service.mapper;

import com.khac_dat.identity_service.dto.request.LoanCreationRequest;
import com.khac_dat.identity_service.dto.response.LoanResponse;
import com.khac_dat.identity_service.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BookMapper.class, MemberMapper.class})
public interface LoanMapper {

    @Mapping(target = "loanDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "member", ignore = true)
    Loan toLoan(LoanCreationRequest request);

    @Mapping(target = "status", expression = "java(loan.getReturnDate() == null ? \"BORROWING\" : \"RETURNED\")")
    LoanResponse toLoanResponse(Loan loan);
}