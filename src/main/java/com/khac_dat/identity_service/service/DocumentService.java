package com.khac_dat.identity_service.service;

import com.khac_dat.identity_service.dto.request.CreateDocumentRequest;
import com.khac_dat.identity_service.dto.response.DocumentResponse;
import com.khac_dat.identity_service.entity.Document;
import com.khac_dat.identity_service.entity.User;
import com.khac_dat.identity_service.enums.DocumentStatus;
import com.khac_dat.identity_service.exception.AppException;
import com.khac_dat.identity_service.exception.ErrorCode;
import com.khac_dat.identity_service.mapper.DocumentMapper;
import com.khac_dat.identity_service.repository.DocumentRepository;
import com.khac_dat.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentService {
    DocumentRepository documentRepository;
    DocumentMapper documentMapper;
    UserRepository userRepository;

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

        return documentMapper.toDocumentResponse(documentRepository.save(doc));
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
        return documentMapper.toDocumentResponse(documentRepository.save(doc));
    }

    public void delete(String id) {
        if (!documentRepository.existsById(id)) {
            throw new AppException(ErrorCode.DOCUMENT_NOT_EXISTED);
        }
        documentRepository.deleteById(id);
    }
}