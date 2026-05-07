package com.example.eureka.auth.credentials;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialSetupTokenMapper {

    @Insert("""
        INSERT INTO credential_setup_tokens(
            token, owner_user_id, employee_user_id,
            owner_username, owner_plain_password,
            employee_username, employee_plain_password,
            send_after, expires_at)
        VALUES(
            #{token}, #{ownerUserId}, #{employeeUserId},
            #{ownerUsername}, #{ownerPlainPassword},
            #{employeeUsername}, #{employeePlainPassword},
            #{sendAfter}, #{expiresAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(CredentialSetupToken setupToken);

    @Select("""
        SELECT id, token, owner_user_id AS ownerUserId, employee_user_id AS employeeUserId,
               owner_username AS ownerUsername, owner_plain_password AS ownerPlainPassword,
               employee_username AS employeeUsername, employee_plain_password AS employeePlainPassword,
               used, email_sent AS emailSent, send_after AS sendAfter,
               expires_at AS expiresAt, created_at AS createdAt
        FROM credential_setup_tokens
        WHERE token = #{token}
    """)
    CredentialSetupToken findByToken(String token);

    @Select("""
        SELECT id, token, owner_user_id AS ownerUserId, employee_user_id AS employeeUserId,
               owner_username AS ownerUsername, owner_plain_password AS ownerPlainPassword,
               employee_username AS employeeUsername, employee_plain_password AS employeePlainPassword,
               used, email_sent AS emailSent, send_after AS sendAfter,
               expires_at AS expiresAt, created_at AS createdAt
        FROM credential_setup_tokens
        WHERE email_sent = FALSE AND send_after <= NOW()
    """)
    List<CredentialSetupToken> findPendingEmailSends();

    @Update("""
        UPDATE credential_setup_tokens
        SET email_sent = TRUE,
            owner_plain_password = NULL,
            employee_plain_password = NULL
        WHERE id = #{id}
    """)
    void markEmailSent(Long id);

    @Update("UPDATE credential_setup_tokens SET used = TRUE WHERE token = #{token}")
    void markUsed(String token);
}
