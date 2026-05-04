package com.khac_dat.identity_service.controller;

import com.khac_dat.identity_service.dto.request.ApiResponse;
import com.khac_dat.identity_service.dto.request.CreateDocumentRequest;
import com.khac_dat.identity_service.dto.request.ShareDocumentRequest;
import com.khac_dat.identity_service.dto.response.DocumentResponse;
import com.khac_dat.identity_service.service.DocumentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentController {
    DocumentService documentService;

    @PostMapping
    @PreAuthorize("hasAuthority('DOC_CREATE')")
    public ApiResponse<DocumentResponse> create(@RequestBody @Valid CreateDocumentRequest request) {
        return ApiResponse.<DocumentResponse>builder().result(documentService.create(request)).build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('DOC_READ')")
    public ApiResponse<List<DocumentResponse>> getAll() {
        // Lưu ý: Bạn cần tạo hàm getAllDocuments() trong DocumentService
        return ApiResponse.<List<DocumentResponse>>builder()
                .result(documentService.getAllDocuments())
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('DOC_READ') and @docSecurity.canAccess(#id, authentication)")
    public ApiResponse<DocumentResponse> get(@PathVariable String id) {
        return ApiResponse.<DocumentResponse>builder().result(documentService.getDocument(id)).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('DOC_UPDATE') and @docSecurity.canAccess(#id, authentication)")
    public ApiResponse<DocumentResponse> update(@PathVariable String id, @RequestBody CreateDocumentRequest request) {
        return ApiResponse.<DocumentResponse>builder().result(documentService.update(id, request)).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DOC_DELETE') and @docSecurity.isOwner(#id, authentication)")
    public ApiResponse<Void> delete(@PathVariable String id) {
        documentService.delete(id);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('DOC_UPDATE') and @docSecurity.isOwner(#id, authentication)")
    public ApiResponse<DocumentResponse> submit(@PathVariable String id) {
        return ApiResponse.<DocumentResponse>builder().result(documentService.submit(id)).build();
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('DOC_APPROVE') and @docSecurity.canApprove(#id, authentication)")
    public ApiResponse<DocumentResponse> approve(@PathVariable String id) {
        return ApiResponse.<DocumentResponse>builder().result(documentService.approve(id)).build();
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('DOC_APPROVE') and @docSecurity.canApprove(#id, authentication)")
    public ApiResponse<DocumentResponse> reject(@PathVariable String id) {
        return ApiResponse.<DocumentResponse>builder().result(documentService.reject(id)).build();
    }

    @PostMapping("/{id}/share")
    @PreAuthorize("hasAnyAuthority('DOC_SHARE', 'DOC_SHARE_CROSS_DEPT') and @docSecurity.isOwner(#id, authentication)")
    public ApiResponse<Void> share(@PathVariable String id, @RequestBody ShareDocumentRequest request) {
        documentService.shareDocument(id, request);
        return ApiResponse.<Void>builder().message("Chia sẻ tài liệu thành công").build();
    }
}