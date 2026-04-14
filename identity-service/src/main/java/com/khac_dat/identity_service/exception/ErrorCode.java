package com.khac_dat.identity_service.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa được phân loại"),
    INVALID_KEY(1001,"Key không hợp lệ"),
    USERNAME_EXISTED(1002,"Tên người dùng đã tồn tại"),
    USERNAME_INVALID(1003,"Tên người dùng phải tối thiếu 3 kí tự"),
    PASSWORD_INVALID(1004,"Mật khẩu phải tối thiểu 8 kí tự"),
    USER_NOT_EXISTED(1005, "Người dùng không tồn tại"),
    UNAUTHENTICATED(1006,"Khong the xac minh"),
    BOOK_EXISTED(2001,"Sach da ton tai"),
    BOOK_NOT_EXISTED(2002,"ko tim thay sach"),
    MEMBER_EXISTED(2003,"thanh vien da ton tai"),
    MEMBER_NOT_EXISTED(2004,"khong tim thay thanh vien"),
    BOOK_ALREADY_RETURNED(2005,"Sach da duoc tra"),
    BOOK_ALREADY_BORROWED(2006,"Sach dang duoc muon"),
    BOOK_NOT_BORROWED(2007,"Sach khong duoc muon"),
    LOAN_NOT_FOUND(2008,"Ko tim thay phieu muon")
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
