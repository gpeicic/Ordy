ALTER TABLE credential_setup_tokens ADD COLUMN owner_username          VARCHAR(255);
ALTER TABLE credential_setup_tokens ADD COLUMN owner_plain_password    VARCHAR(255);
ALTER TABLE credential_setup_tokens ADD COLUMN employee_username       VARCHAR(255);
ALTER TABLE credential_setup_tokens ADD COLUMN employee_plain_password VARCHAR(255);
ALTER TABLE credential_setup_tokens ADD COLUMN email_sent              TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE credential_setup_tokens ADD COLUMN send_after              DATETIME;
