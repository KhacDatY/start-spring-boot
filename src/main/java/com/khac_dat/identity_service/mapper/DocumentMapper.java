package com.khac_dat.identity_service.mapper;

import com.khac_dat.identity_service.dto.request.CreateDocumentRequest;
import com.khac_dat.identity_service.dto.response.DocumentResponse;
import com.khac_dat.identity_service.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    @Mapping(source = "owner.fullName", target = "ownerName")
    @Mapping(source = "department.name", target = "departmentName")
    DocumentResponse toDocumentResponse(Document document);

    void updateDocument(@MappingTarget Document document, CreateDocumentRequest request);
}
