package com.khac_dat.identity_service.mapper;

import com.khac_dat.identity_service.dto.request.MemberCreationRequest;
import com.khac_dat.identity_service.dto.request.MemberUpdateRequest;
import com.khac_dat.identity_service.dto.response.MemberResponse;
import com.khac_dat.identity_service.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member toMember(MemberCreationRequest request);
    MemberResponse toMemberResponse(Member member);
    void updateMember(@MappingTarget Member member, MemberUpdateRequest request);
}
