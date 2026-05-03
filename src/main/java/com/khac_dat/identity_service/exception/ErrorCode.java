package com.khac_dat.identity_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa được phân loại", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001,"Key không hợp lệ", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1002,"Tên người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003,"Tên người dùng phải tối thiếu 3 kí tự", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004,"Mật khẩu phải tối thiểu 8 kí tự", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006,"Khong the xac minh",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007,"Ban khong co quyen truy cap",HttpStatus.FORBIDDEN),
    ROLE_NOT_EXISTED(1008,"Khong tim thay role", HttpStatus.BAD_REQUEST),
    ROLE_EXISTED(1009,"Role da ton tai", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1010,"Permission khong ton tai", HttpStatus.BAD_REQUEST),
    PERMISSION_EXISTED(1011,"Permission da ton tai", HttpStatus.BAD_REQUEST),
    ;
    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
