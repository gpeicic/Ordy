package com.example.eureka.merInvoices;

import com.example.eureka.exception.ResourceNotFoundException;
import com.example.eureka.merAuth.MerAuthService;
import com.example.eureka.sessionToken.SessionToken;
import com.example.eureka.sessionToken.SessionTokenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class MerTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(MerTokenProvider.class);

    private final SessionTokenMapper sessionTokenMapper;
    private final MerAuthService merAuthService;
    private final TextEncryptor encryptor;

    public MerTokenProvider(SessionTokenMapper sessionTokenMapper,
                            MerAuthService merAuthService,
                            TextEncryptor encryptor) {
        this.sessionTokenMapper = sessionTokenMapper;
        this.merAuthService = merAuthService;
        this.encryptor = encryptor;
    }

    public SessionToken getValidToken(Long companyId) {
        SessionToken token = loadToken(companyId);
        if (isExpired(token)) {
            token = refresh(companyId);
        }
        token.setAccessToken(encryptor.decrypt(token.getAccessToken()));
        return token;
    }

    private SessionToken loadToken(Long companyId) {
        SessionToken token = sessionTokenMapper.findByCompanyId(companyId);
        if (token == null) {
            log.error("MER token nije pronađen za kompaniju: {}", companyId);
            throw new ResourceNotFoundException("MER token nije pronađen za kompaniju: " + companyId);
        }
        return token;
    }

    private SessionToken refresh(Long companyId) {
        log.info("MER token istekao za kompaniju: {}, refresham...", companyId);
        merAuthService.loginCompany(companyId);
        SessionToken token = sessionTokenMapper.findByCompanyId(companyId);
        log.info("MER token refreshan za kompaniju: {}", companyId);
        return token;
    }

    private boolean isExpired(SessionToken token) {
        return token.getExpiration() == null ||
                LocalDateTime.now(ZoneOffset.UTC).isAfter(token.getExpiration().minusMinutes(5));
    }
}