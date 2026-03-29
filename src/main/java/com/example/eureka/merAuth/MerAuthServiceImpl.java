package com.example.eureka.merAuth;

import com.example.eureka.company.Company;
import com.example.eureka.company.CompanyMapper;
import com.example.eureka.sessionToken.SessionToken;
import com.example.eureka.sessionToken.SessionTokenMapper;
import org.springframework.stereotype.Service;

@Service
public class MerAuthServiceImpl implements MerAuthService {

    private final CompanyMapper companyMapper;
    private final SessionTokenMapper tokenMapper;
    private final MerClient merClient;

    public MerAuthServiceImpl(
            CompanyMapper companyMapper,
            SessionTokenMapper tokenMapper,
            MerClient merClient
    ) {
        this.companyMapper = companyMapper;
        this.tokenMapper = tokenMapper;
        this.merClient = merClient;
    }

    @Override
    public void loginCompany(Long companyId) {

        Company company = companyMapper.findById(companyId);

        MerLoginRequest request = new MerLoginRequest();
        request.setUsername(company.getMerEmail());
        request.setPassword(company.getMerPassword());

        MerLoginResponse response = merClient.login(request);

        SessionToken token = new SessionToken();
        token.setCompanyId(companyId);
        token.setAccessToken(response.getAccessToken());
        token.setRefreshToken(response.getRefreshToken());
        token.setExpiration(response.getExpirationDate());
        tokenMapper.upsert(token);
    }
}