package com.example.eureka.merAuth;

import com.example.eureka.company.Company;
import com.example.eureka.company.CompanyMapper;
import com.example.eureka.exception.ResourceNotFoundException;
import com.example.eureka.exception.ValidationException;
import com.example.eureka.sessionToken.SessionToken;
import com.example.eureka.sessionToken.SessionTokenMapper;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class MerAuthServiceImpl implements MerAuthService {

    private final CompanyMapper companyMapper;
    private final SessionTokenMapper tokenMapper;
    private final MerClient merClient;
    private final TextEncryptor encryptor;

    public MerAuthServiceImpl(CompanyMapper companyMapper,
                              SessionTokenMapper tokenMapper,
                              MerClient merClient,
                              TextEncryptor encryptor) {
        this.companyMapper = companyMapper;
        this.tokenMapper = tokenMapper;
        this.merClient = merClient;
        this.encryptor = encryptor;
    }

    @Override
    public void loginCompany(Long companyId) {
        Company company = companyMapper.findById(companyId);
        if (company == null) {
            throw new ResourceNotFoundException("Kompanija nije pronađena: " + companyId);
        }
        if (company.getMerEmail() == null || company.getMerPassword() == null) {
            throw new ValidationException("MER kredencijali nisu postavljeni za kompaniju: " + companyId);
        }
        MerLoginResponse response = merClient.login(buildLoginRequest(company));
        if (response == null || response.getAccessToken() == null) {
            throw new ValidationException("MER login nije uspio za kompaniju: " + companyId);
        }
        tokenMapper.upsert(buildSessionToken(companyId, response));
    }

    private MerLoginRequest buildLoginRequest(Company company) {
        MerLoginRequest request = new MerLoginRequest();
        request.setUsername(company.getMerEmail());
        request.setPassword(encryptor.decrypt(company.getMerPassword()));
        return request;
    }

    private SessionToken buildSessionToken(Long companyId, MerLoginResponse response) {
        SessionToken token = new SessionToken();
        token.setCompanyId(companyId);
        token.setAccessToken(encryptor.encrypt(response.getAccessToken()));
        token.setRefreshToken(encryptor.encrypt(response.getRefreshToken()));
        token.setExpiration(response.getExpirationDate());
        return token;
    }
}