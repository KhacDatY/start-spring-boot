package com.khac_dat.identity_service.enums;

public enum AuditAction {
    LOGIN_SUCCESS, LOGIN_FAILED, LOGOUT,
    DOC_CREATE, DOC_UPDATE, DOC_DELETE,
    DOC_SUBMIT, DOC_APPROVE, DOC_REJECT, DOC_SHARE,
    ROLE_CHANGED
}