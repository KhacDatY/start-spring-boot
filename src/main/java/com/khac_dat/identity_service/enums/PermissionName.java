package com.khac_dat.identity_service.enums;

public enum PermissionName {
    // USER
    USER_READ,
    USER_WRITE,
    USER_DELETE,

    // ROLE
    ROLE_READ,
    ROLE_WRITE,

    // DOCUMENT
    DOC_CREATE,
    DOC_READ,
    DOC_UPDATE,
    DOC_DELETE,
    DOC_APPROVE,
    DOC_SHARE,
    DOC_SHARE_CROSS_DEPT,

    // AUDIT
    AUDIT_READ
}
