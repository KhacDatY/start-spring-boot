package com.khac_dat.identity_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa được phân loại", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Key không hợp lệ", HttpStatus.BAD_REQUEST),

    EMAIL_EXISTED(1002, "Email đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Tên người dùng phải tối thiểu 3 kí tự", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Mật khẩu phải tối thiểu 8 kí tự", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Không thể xác minh thông tin đăng nhập", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Bạn không có quyền truy cập tài nguyên này", HttpStatus.FORBIDDEN),

    TOKEN_EXPIRED(1013, "Token đã hết hạn, vui lòng đăng nhập lại", HttpStatus.UNAUTHORIZED),
    TOKEN_REVOKED(1014, "Token đã bị thu hồi hoặc không hợp lệ", HttpStatus.UNAUTHORIZED),

    ROLE_NOT_EXISTED(1008, "Không tìm thấy Role", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(1009, "Role đã tồn tại", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1010, "Permission không tồn tại", HttpStatus.NOT_FOUND),
    PERMISSION_EXISTED(1011, "Permission đã tồn tại", HttpStatus.BAD_REQUEST),

    DEPARTMENT_EXISTED(1012, "Phòng ban đã tồn tại", HttpStatus.BAD_REQUEST),
    DEPARTMENT_NOT_EXISTED(1015, "Phòng ban không tồn tại", HttpStatus.NOT_FOUND),

    DOCUMENT_NOT_EXISTED(1016, "Tài liệu không tồn tại", HttpStatus.NOT_FOUND),
    DOCUMENT_APPROVED_IMMUTABLE(1017, "Tài liệu đã được duyệt, không thể chỉnh sửa", HttpStatus.BAD_REQUEST),
    CANNOT_APPROVE_OWN_DOCUMENT(1018, "Không thể tự phê duyệt tài liệu do chính mình tạo", HttpStatus.BAD_REQUEST),
    INVALID_DOCUMENT_STATUS(1019, "Trạng thái tài liệu không hợp lệ cho thao tác này", HttpStatus.BAD_REQUEST),
    DOCUMENT_ALREADY_SHARED(1020, "Tài liệu đã được chia sẻ cho đối tượng này từ trước", HttpStatus.BAD_REQUEST),

    REFRESH_TOKEN_INVALID(1021, "Refresh token không hợp lệ hoặc không tồn tại", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED(1022, "Refresh token đã hết hạn, vui lòng đăng nhập lại", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_REVOKED(1023, "Refresh token đã bị thu hồi hoặc đã đăng xuất", HttpStatus.UNAUTHORIZED),
    ;
    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
