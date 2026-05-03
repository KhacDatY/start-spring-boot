package com.khac_dat.identity_service.repository;

import com.khac_dat.identity_service.entity.DocumentShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentShareRepository extends JpaRepository<DocumentShare, String> {
}