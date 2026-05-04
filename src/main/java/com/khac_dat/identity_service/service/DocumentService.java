package com.khac_dat.identity_service.service;

import com.khac_dat.identity_service.dto.request.CreateDocumentRequest;
import com.khac_dat.identity_service.dto.request.ShareDocumentRequest;
import com.khac_dat.identity_service.dto.response.DocumentResponse;
import com.khac_dat.identity_service.entity.Department;
import com.khac_dat.identity_service.entity.Document;
import com.khac_dat.identity_service.entity.DocumentShare;
import com.khac_dat.identity_service.entity.User;
import com.khac_dat.identity_service.enums.AuditAction;
import com.khac_dat.identity_service.enums.DocumentStatus;
import com.khac_dat.identity_service.exception.AppException;
import com.khac_dat.identity_service.exception.ErrorCode;
import com.khac_dat.identity_service.mapper.DocumentMapper;
import com.khac_dat.identity_service.repository.DepartmentRepository;
import com.khac_dat.identity_service.repository.DocumentRepository;
import com.khac_dat.identity_service.repository.DocumentShareRepository;
import com.khac_dat.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final UserRepository userRepository;

    private final DocumentShareRepository documentShareRepository;
    private final DepartmentRepository departmentRepository;

    private final AuditLogService auditLogService;

    public DocumentResponse create(CreateDocumentRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Document doc = Document.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .isPublicInDepartment(request.isPublicInDepartment())
                .owner(owner)
                .department(owner.getDepartment())
                .status(DocumentStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        auditLogService.logAction(AuditAction.DOC_CREATE, "DOCUMENT", doc.getId(), "Tạo tài liệu mới: " + request.getTitle());
        return documentMapper.toDocumentResponse(documentRepository.save(doc));
    }

    public List<DocumentResponse> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(documentMapper::toDocumentResponse)
                .toList();
    }

    public DocumentResponse getDocument(String id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DOCUMENT_NOT_EXISTED));
        return documentMapper.toDocumentResponse(document);
    }

    public DocumentResponse update(String id, CreateDocumentRequest request) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DOCUMENT_NOT_EXISTED));

        if (doc.getStatus() == DocumentStatus.APPROVED) {
            throw new AppException(ErrorCode.DOCUMENT_APPROVED_IMMUTABLE);
        }

        documentMapper.updateDocument(doc, request);
        doc.setUpdatedAt(LocalDateTime.now());
        auditLogService.logAction(AuditAction.DOC_UPDATE, "DOCUMENT", doc.getId(), "Cập nhật nội dung tài liệu");
        return documentMapper.toDocumentResponse(documentRepository.save(doc));
    }

    public void delete(String id) {
        if (!documentRepository.existsById(id)) {
            throw new AppException(ErrorCode.DOCUMENT_NOT_EXISTED);
        }
        documentRepository.deleteById(id);
        auditLogService.logAction(AuditAction.DOC_DELETE, "DOCUMENT", id, "Xóa tài liệu hệ thống");
    }

    public DocumentResponse submit(String id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DOCUMENT_NOT_EXISTED));

        // check trang thai
        if (doc.getStatus() != DocumentStatus.DRAFT && doc.getStatus() != DocumentStatus.REJECTED) {
            throw new AppException(ErrorCode.INVALID_DOCUMENT_STATUS);
        }

        doc.setStatus(DocumentStatus.PENDING); // cho duyet
        doc.setUpdatedAt(LocalDateTime.now());
        auditLogService.logAction(AuditAction.DOC_SUBMIT, "DOCUMENT", doc.getId(), "Gửi tài liệu chờ duyệt");
        return documentMapper.toDocumentResponse(documentRepository.save(doc));
    }

    public DocumentResponse approve(String id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DOCUMENT_NOT_EXISTED));

        if (doc.getStatus() != DocumentStatus.PENDING) {
            throw new AppException(ErrorCode.INVALID_DOCUMENT_STATUS);
        }

        doc.setStatus(DocumentStatus.APPROVED);
        doc.setUpdatedAt(LocalDateTime.now());
        auditLogService.logAction(AuditAction.DOC_APPROVE, "DOCUMENT", doc.getId(), "Đã duyệt tài liệu");
        return documentMapper.toDocumentResponse(documentRepository.save(doc));
    }

    public DocumentResponse reject(String id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DOCUMENT_NOT_EXISTED));

        if (doc.getStatus() != DocumentStatus.PENDING) {
            throw new AppException(ErrorCode.INVALID_DOCUMENT_STATUS);
        }

        doc.setStatus(DocumentStatus.REJECTED);
        doc.setUpdatedAt(LocalDateTime.now());
        auditLogService.logAction(AuditAction.DOC_REJECT, "DOCUMENT", doc.getId(), "Đã từ chối tài liệu");
        return documentMapper.toDocumentResponse(documentRepository.save(doc));
    }

    public void shareDocument(String documentId, ShareDocumentRequest request) {
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new AppException(ErrorCode.DOCUMENT_NOT_EXISTED));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User sharedBy = userRepository.findByEmail(email).orElseThrow();

        DocumentShare share = DocumentShare.builder()
                .document(doc)
                .sharedBy(sharedBy)
                .createdAt(LocalDateTime.now())
                .build();

        if (request.getTargetUserId() != null) {
            User targetUser = userRepository.findById(request.getTargetUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            share.setSharedWithUser(targetUser);
        }

        if (request.getTargetDepartmentId() != null) {
            Department targetDept = departmentRepository.findById(request.getTargetDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
            share.setSharedWithDepartment(targetDept);
        }

        documentShareRepository.save(share);
        auditLogService.logAction(AuditAction.DOC_SHARE, "DOCUMENT", documentId, "Chia sẻ tài liệu cho người khác");
    }
}