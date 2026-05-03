package com.khac_dat.identity_service.security.permission;

import com.khac_dat.identity_service.entity.Document;
import com.khac_dat.identity_service.entity.User;
import com.khac_dat.identity_service.repository.DocumentRepository;
import com.khac_dat.identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("docSecurity")
@RequiredArgsConstructor
public class DocumentSecurityService {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public boolean canAccess(String documentId, Authentication auth) {
        User currentUser = getCurrentUser(auth);
        Document doc = documentRepository.findById(documentId).orElseThrow();

        if (hasRole(auth, "SUPER_ADMIN") || hasRole(auth, "ADMIN")) return true;

        if (hasRole(auth, "MANAGER")) {
            return doc.getDepartment().getId().equals(currentUser.getDepartment().getId());
        }

        boolean isOwner = doc.getOwner().getId().equals(currentUser.getId());
        boolean isPublicInDept = doc.isPublicInDepartment() &&
                doc.getDepartment().getId().equals(currentUser.getDepartment().getId());

        return isOwner || isPublicInDept;
    }

    public boolean canApprove(String documentId, Authentication auth) {
        User currentUser = getCurrentUser(auth);
        Document doc = documentRepository.findById(documentId).orElseThrow();

        if (hasRole(auth, "SUPER_ADMIN")) return true;

        boolean sameDept = doc.getDepartment().getId().equals(currentUser.getDepartment().getId());

        boolean isNotOwner = !doc.getOwner().getId().equals(currentUser.getId());

        return hasRole(auth, "MANAGER") && sameDept && isNotOwner;
    }

    private User getCurrentUser(Authentication auth) {
        return userRepository.findByEmail(auth.getName()).orElseThrow();
    }

    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}
