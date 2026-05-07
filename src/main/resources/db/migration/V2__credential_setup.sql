ALTER TABLE users ADD COLUMN IF NOT EXISTS email VARCHAR(255);
CREATE TABLE IF NOT EXISTS credential_setup_tokens (
    id                      BIGSERIAL PRIMARY KEY,
    token                   VARCHAR(36)  NOT NULL UNIQUE,
    owner_user_id           BIGINT       NOT NULL REFERENCES users(id),
    employee_user_id        BIGINT       NOT NULL REFERENCES users(id),
    owner_username          VARCHAR(255),
    owner_plain_password    VARCHAR(255),
    employee_username       VARCHAR(255),
    employee_plain_password VARCHAR(255),
    used                    BOOLEAN      NOT NULL DEFAULT FALSE,
    email_sent              BOOLEAN      NOT NULL DEFAULT FALSE,
    send_after              TIMESTAMP    NOT NULL,
    expires_at              TIMESTAMP    NOT NULL,
    created_at              TIMESTAMP    DEFAULT NOW()
);
