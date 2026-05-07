ALTER TABLE credential_setup_tokens ADD COLUMN IF NOT EXISTS owner_username          VARCHAR(255);
ALTER TABLE credential_setup_tokens ADD COLUMN IF NOT EXISTS owner_plain_password    VARCHAR(255);
ALTER TABLE credential_setup_tokens ADD COLUMN IF NOT EXISTS employee_username       VARCHAR(255);
ALTER TABLE credential_setup_tokens ADD COLUMN IF NOT EXISTS employee_plain_password VARCHAR(255);
ALTER TABLE credential_setup_tokens ADD COLUMN IF NOT EXISTS email_sent              BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE credential_setup_tokens ADD COLUMN IF NOT EXISTS send_after              TIMESTAMP;
