package com.example.eureka.sessionToken;

import org.apache.ibatis.annotations.*;

@Mapper
public interface SessionTokenMapper {

    @Select("SELECT * FROM session_tokens WHERE company_id = #{companyId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "companyId", column = "company_id"),
            @Result(property = "accessToken", column = "access_token"),
            @Result(property = "refreshToken", column = "refresh_token"),
            @Result(property = "expiration", column = "expiration")
    })
    SessionToken findByCompanyId(Long companyId);

    @Insert("INSERT INTO session_tokens(company_id, access_token, refresh_token, expiration) " +
            "VALUES(#{companyId}, #{accessToken}, #{refreshToken}, #{expiration}) " +
            "ON CONFLICT (company_id) DO UPDATE SET " +
            "access_token = EXCLUDED.access_token, " +
            "refresh_token = EXCLUDED.refresh_token, " +
            "expiration = EXCLUDED.expiration")
    void upsert(SessionToken token);
}