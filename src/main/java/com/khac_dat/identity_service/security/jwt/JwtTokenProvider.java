package com.khac_dat.identity_service.security.jwt;

import com.khac_dat.identity_service.entity.Role;
import com.khac_dat.identity_service.entity.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;


    public String generateToken(User user) {
        try {
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
                    .claim("roles", roles)
                    .claim("permissions", permissions)
                    .build();

            Payload payload = new Payload(jwtClaimsSet.toJSONObject());
            JWSObject jwsObject = new JWSObject(header, payload);

            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();

        } catch (KeyLengthException e) {
            log.error("Key length is insufficient for HS512", e);
            throw new RuntimeException("Internal Server Error: Security key issues", e);
        } catch (JOSEException e) {
            log.error("Khong the tao token", e);
            throw new RuntimeException("Loi tao token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            boolean isVerified = signedJWT.verify(verifier);

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean isNotExpired = expirationTime.after(new Date());

            return isVerified && isNotExpired;

        } catch (JOSEException | ParseException e) {
            log.error("Khong the xac thuc token: {}", e.getMessage());
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            log.error("Khong the lay subject");
            return null;
        }
    }
}