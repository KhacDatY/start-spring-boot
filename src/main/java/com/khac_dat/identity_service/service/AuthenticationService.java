package com.khac_dat.identity_service.service;

import com.khac_dat.identity_service.dto.request.AuthenticationRequest;
import com.khac_dat.identity_service.dto.request.IntrospectRequest;
import com.khac_dat.identity_service.dto.response.AuthenticationResponse;
import com.khac_dat.identity_service.dto.response.IntrospectResponse;
import com.khac_dat.identity_service.entity.Role;
import com.khac_dat.identity_service.entity.User;
import com.khac_dat.identity_service.exception.AppException;
import com.khac_dat.identity_service.exception.ErrorCode;
import com.khac_dat.identity_service.repository.UserRepository;
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
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;


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

        if(!authenticated || !user.isEnabled())
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        try {
            // Bắt buộc phải có try-catch ở đây vì generateToken ném ra KeyLengthException
            var token = generateToken(user);

            return AuthenticationResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();
        } catch (KeyLengthException e) {
            // Log lỗi và ném ra một RuntimeException để Spring xử lý
            log.error("Key length is insufficient for HS512", e);
            throw new RuntimeException("Internal Server Error: Security key issues", e);
        }

    }

    private String generateToken(User user) throws KeyLengthException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        Set<String> permissions = new HashSet<>();
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission ->
                            permissions.add(permission.getName())
                    );
                }
            });
        }

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("khacdat.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("roles", roles)             // Nạp mảng roles
                .claim("permissions", permissions) // Nạp mảng permissions
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }


}
