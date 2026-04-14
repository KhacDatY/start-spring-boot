package com.khac_dat.identity_service.repository;

import com.khac_dat.identity_service.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
