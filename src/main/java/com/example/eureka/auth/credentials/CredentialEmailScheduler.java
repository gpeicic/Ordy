package com.example.eureka.auth.credentials;

import com.example.eureka.auth.credentials.email.EmailService;
import com.example.eureka.user.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CredentialEmailScheduler {

    private static final Logger log = LoggerFactory.getLogger(CredentialEmailScheduler.class);

    private final CredentialSetupTokenMapper tokenMapper;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public CredentialEmailScheduler(CredentialSetupTokenMapper tokenMapper,
                                    UserMapper userMapper,
                                    EmailService emailService) {
        this.tokenMapper = tokenMapper;
        this.userMapper = userMapper;
        this.emailService = emailService;
    }

    // Provjerava svakih 10 minuta
    @Scheduled(fixedDelay = 600_000)
    public void sendPendingCredentialEmails() {
        tokenMapper.findPendingEmailSends().forEach(setupToken -> {
            try {
                String email = userMapper.findEmailById(setupToken.getOwnerUserId());
                if (email == null) {
                    log.warn("Email nije pronađen za userId: {}", setupToken.getOwnerUserId());
                    return;
                }

                String setupLink = frontendUrl + "/setup-credentials?token=" + setupToken.getToken();

                emailService.sendCredentials(
                        email,
                        setupToken.getOwnerUsername(),
                        setupToken.getOwnerPlainPassword(),
                        setupToken.getEmployeeUsername(),
                        setupToken.getEmployeePlainPassword(),
                        setupLink
                );

                tokenMapper.markEmailSent(setupToken.getId()); // briše plain passworde iz DB-a
                log.info("Credentials email poslan na: {}", email);

            } catch (Exception e) {
                log.error("Greška pri slanju emaila za tokenId: {}", setupToken.getId(), e);
            }
        });
    }
}

