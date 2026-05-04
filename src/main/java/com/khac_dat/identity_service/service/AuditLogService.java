package com.khac_dat.identity_service.service;

import com.khac_dat.identity_service.dto.response.AuditLogResponse;
import com.khac_dat.identity_service.entity.AuditLog;
import com.khac_dat.identity_service.enums.AuditAction;
import com.khac_dat.identity_service.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void logAction(AuditAction action, String resourceType, String resourceId, String description) {
        try {
            String actorId = "SYSTEM";
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
                actorId = authentication.getName();
            }

            String ipAddress = "UNKNOWN";
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                ipAddress = request.getHeader("X-Forwarded-For");
                if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getRemoteAddr();
                }
            }

            AuditLog auditLog = AuditLog.builder()
                    .actorId(actorId)
                    .action(action)
                    .resourceType(resourceType)
                    .resourceId(resourceId)
                    .description(description)
                    .createdAt(LocalDateTime.now())
                    .ipAddress(ipAddress)
                    .build();

            auditLogRepository.save(auditLog);

        } catch (Exception e) {
            log.error("Lỗi khi ghi Audit Log: {}", e.getMessage());
        }
    }


    public List<AuditLogResponse> getAllLogs() {
        return auditLogRepository.findAll().stream()
                .map(logItem -> AuditLogResponse.builder()
                        .id(logItem.getId())
                        .actorId(logItem.getActorId())
                        .action(logItem.getAction() != null ? logItem.getAction().name() : null)
                        .resourceType(logItem.getResourceType())
                        .resourceId(logItem.getResourceId())
                        .description(logItem.getDescription())
                        .createdAt(logItem.getCreatedAt())
                        .ipAddress(logItem.getIpAddress())
                        .build())
                .toList();
    }
}