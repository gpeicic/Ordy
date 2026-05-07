package com.example.eureka.auth.credentials;

import com.example.eureka.auth.credentials.dto.CredentialSetupRequest;
import com.example.eureka.exception.ResourceNotFoundException;
import com.example.eureka.exception.ValidationException;
import com.example.eureka.user.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CredentialSetupServiceImpl implements CredentialSetupService{

    private final CredentialSetupTokenMapper tokenMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public CredentialSetupServiceImpl(CredentialSetupTokenMapper tokenMapper,
                                      UserMapper userMapper,
                                      PasswordEncoder passwordEncoder) {
        this.tokenMapper = tokenMapper;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void setupCredentials(CredentialSetupRequest request) {
        CredentialSetupToken setupToken = tokenMapper.findByToken(request.getToken());

        if (setupToken == null) {
            throw new ResourceNotFoundException("Token nije pronađen");
        }
        if (setupToken.isUsed()) {
            throw new ValidationException("Token je već iskorišten");
        }
        if (setupToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Token je istekao");
        }

        // Check uniqueness of new usernames
        if (userMapper.findByUsername(request.getOwnerUsername()) != null) {
            throw new ValidationException("Korisničko ime vlasnika već postoji");
        }
        if (userMapper.findByUsername(request.getEmployeeUsername()) != null) {
            throw new ValidationException("Korisničko ime zaposlenika već postoji");
        }

        userMapper.updateUsernameAndPassword(
                setupToken.getOwnerUserId(),
                request.getOwnerUsername(),
                passwordEncoder.encode(request.getOwnerPassword())
        );
        userMapper.updateUsernameAndPassword(
                setupToken.getEmployeeUserId(),
                request.getEmployeeUsername(),
                passwordEncoder.encode(request.getEmployeePassword())
        );

        tokenMapper.markUsed(request.getToken());
    }
}
