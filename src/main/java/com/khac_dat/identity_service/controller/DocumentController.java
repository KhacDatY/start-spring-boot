package com.khac_dat.identity_service.controller;

import com.khac_dat.identity_service.dto.request.ApiResponse;
import com.khac_dat.identity_service.dto.request.CreateDocumentRequest;
import com.khac_dat.identity_service.dto.response.DocumentResponse;
import com.khac_dat.identity_service.service.DocumentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentController {
    DocumentService documentService;

    @PostMapping
    @PreAuthorize("hasAuthority('DOC_CREATE')")
    public ApiResponse<DocumentResponse> create(@RequestBody @Valid CreateDocumentRequest request) {
        return ApiResponse.<DocumentResponse>builder().result(documentService.create(request)).build();
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
}