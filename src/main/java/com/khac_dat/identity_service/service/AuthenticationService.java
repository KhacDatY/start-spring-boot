package com.khac_dat.identity_service.service;

import com.khac_dat.identity_service.dto.request.AuthenticationRequest;
import com.khac_dat.identity_service.dto.request.IntrospectRequest;
import com.khac_dat.identity_service.dto.request.RefreshTokenRequest;
import com.khac_dat.identity_service.dto.response.AuthenticationResponse;
import com.khac_dat.identity_service.dto.response.IntrospectResponse;
import com.khac_dat.identity_service.entity.RefreshToken;
import com.khac_dat.identity_service.entity.Role;
import com.khac_dat.identity_service.entity.User;
import com.khac_dat.identity_service.enums.AuditAction;
import com.khac_dat.identity_service.exception.AppException;
import com.khac_dat.identity_service.exception.ErrorCode;
import com.khac_dat.identity_service.repository.UserRepository;
import com.khac_dat.identity_service.security.jwt.JwtTokenProvider;
import com.khac_dat.identity_service.security.jwt.RefreshTokenService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final AuditLogService auditLogService;


    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expityTime.after(new Date()))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated || !user.isEnabled()) {
            auditLogService.logAction(AuditAction.LOGIN_FAILED, "USER", request.getEmail(), "Sai mật khẩu hoặc tài khoản bị khóa");
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = jwtTokenProvider.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        auditLogService.logAction(AuditAction.LOGIN_SUCCESS, "USER", user.getId(), "Đăng nhập thành công");
        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
                .authenticated(true)
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenProvider.generateToken(user);
                    return AuthenticationResponse.builder()
                            .token(token)
                            .refreshToken(request.getRefreshToken())
                            .authenticated(true)
                            .build();
                })
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_INVALID));
    }

    public void logout(RefreshTokenRequest request) {
        auditLogService.logAction(AuditAction.LOGOUT, "USER", null, "Đăng xuất khỏi hệ thống");
        refreshTokenService.revokeToken(request.getRefreshToken());
    }


}
