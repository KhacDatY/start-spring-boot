package com.khac_dat.identity_service.service;

import com.khac_dat.identity_service.dto.request.MemberCreationRequest;
import com.khac_dat.identity_service.dto.request.MemberUpdateRequest;
import com.khac_dat.identity_service.dto.response.MemberResponse;
import com.khac_dat.identity_service.entity.Member;
import com.khac_dat.identity_service.exception.AppException;
import com.khac_dat.identity_service.exception.ErrorCode;
import com.khac_dat.identity_service.mapper.MemberMapper;
import com.khac_dat.identity_service.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MemberService {
    MemberRepository memberRepository;
    MemberMapper memberMapper;

    public MemberResponse createMember(MemberCreationRequest request){
        // Kiểm tra xem mã thành viên đã tồn tại chưa
        if(memberRepository.existsById(request.getId())){
            throw new AppException(ErrorCode.MEMBER_EXISTED);
        }

        Member member = memberMapper.toMember(request);
        return memberMapper.toMemberResponse(memberRepository.save(member));
    }

    public MemberResponse updateMember(String memberId, MemberUpdateRequest request){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_EXISTED));

        memberMapper.updateMember(member, request);
        return memberMapper.toMemberResponse(memberRepository.save(member));
    }

    public List<MemberResponse> getMembers(){
        return memberRepository.findAll().stream()
                .map(memberMapper::toMemberResponse)
                .toList();
    }

    public MemberResponse getMember(String memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_EXISTED));
        return memberMapper.toMemberResponse(member);
    }

    public void deleteMember(String memberId){
        if (!memberRepository.existsById(memberId)) {
            throw new AppException(ErrorCode.MEMBER_NOT_EXISTED);
        }
        memberRepository.deleteById(memberId);
    }
}