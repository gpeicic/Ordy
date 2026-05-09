ALTER TABLE users ADD COLUMN email VARCHAR(255);

CREATE TABLE IF NOT EXISTS credential_setup_tokens (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    token            VARCHAR(36)  NOT NULL UNIQUE,
    owner_user_id    BIGINT       NOT NULL,
    employee_user_id BIGINT       NOT NULL,
    used             TINYINT(1)   NOT NULL DEFAULT 0,
    expires_at       DATETIME     NOT NULL,
    created_at       DATETIME     DEFAULT NOW(),
    CONSTRAINT fk_cst_owner    FOREIGN KEY (owner_user_id)    REFERENCES users(id),
    CONSTRAINT fk_cst_employee FOREIGN KEY (employee_user_id) REFERENCES users(id)
);
