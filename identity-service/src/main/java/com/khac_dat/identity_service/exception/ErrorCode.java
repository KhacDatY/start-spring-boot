package com.khac_dat.identity_service.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa được phân loại"),
    INVALID_KEY(1001,"Key không hợp lệ"),
    USERNAME_EXISTED(1002,"Tên người dùng đã tồn tại"),
    USERNAME_INVALID(1003,"Tên người dùng phải tối thiếu 3 kí tự"),
    PASSWORD_INVALID(1004,"Mật khẩu phải tối thiểu 8 kí tự"),
    USER_NOT_EXISTED(1005, "Người dùng không tồn tại")
    ;
    private int code;
    private String message;

    ErrorCode(int code,String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
