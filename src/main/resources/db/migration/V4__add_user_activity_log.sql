-- ========================
-- USER_ACTIVITY_LOG
-- ========================
CREATE TABLE IF NOT EXISTS user_activity_log (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    company_id BIGINT       NOT NULL,
    action     VARCHAR(100) NOT NULL,
    created_at DATETIME     DEFAULT NOW(),
    CONSTRAINT fk_ual_user    FOREIGN KEY (user_id)    REFERENCES users(id),
    CONSTRAINT fk_ual_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

