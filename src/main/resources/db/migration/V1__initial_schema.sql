-- ========================
-- ROLES
-- ========================
CREATE TABLE IF NOT EXISTS roles (
                                     id   BIGSERIAL PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_USER')
ON CONFLICT (name) DO NOTHING;

-- ========================
-- USERS
-- ========================
CREATE TABLE IF NOT EXISTS users (
                                     id       BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     role_id  BIGINT NOT NULL REFERENCES roles(id)
);

-- ========================
-- COMPANIES
-- ========================
CREATE TABLE IF NOT EXISTS companies (
                                         id           BIGSERIAL PRIMARY KEY,
                                         name         VARCHAR(255),
                                         mer_email    VARCHAR(255),
                                         mer_password VARCHAR(255),
                                         oib          VARCHAR(20)  UNIQUE,
                                         address      VARCHAR(255),
                                         city         VARCHAR(100),
                                         postal_code  VARCHAR(20)
);

-- ========================
-- USER_COMPANIES (junction)
-- ========================
CREATE TABLE IF NOT EXISTS user_companies (
                                              user_id    BIGINT NOT NULL REFERENCES users(id),
                                              company_id BIGINT NOT NULL REFERENCES companies(id),
                                              PRIMARY KEY (user_id, company_id)
);

-- ========================
-- SUPPLIERS
-- ========================
CREATE TABLE IF NOT EXISTS suppliers (
                                         id    BIGSERIAL PRIMARY KEY,
                                         oib   VARCHAR(20)  UNIQUE,
                                         name  VARCHAR(255) NOT NULL,
                                         email VARCHAR(255)
);

-- ========================
-- COMPANY_SUPPLIERS (junction)
-- ========================
CREATE TABLE IF NOT EXISTS company_suppliers (
                                                 company_id  BIGINT NOT NULL REFERENCES companies(id),
                                                 supplier_id BIGINT NOT NULL REFERENCES suppliers(id),
                                                 PRIMARY KEY (company_id, supplier_id)
);

-- ========================
-- BLOCKED_SUPPLIERS
-- ========================
CREATE TABLE IF NOT EXISTS blocked_suppliers (
                                                 id   BIGSERIAL PRIMARY KEY,
                                                 oib  VARCHAR(20)  UNIQUE NOT NULL,
                                                 name VARCHAR(255) NOT NULL
);

-- ========================
-- CATALOGUE_ITEMS
-- ========================
CREATE TABLE IF NOT EXISTS catalogue_items (
                                               id          BIGSERIAL PRIMARY KEY,
                                               supplier_id BIGINT         NOT NULL REFERENCES suppliers(id),
                                               code        VARCHAR(100)   NOT NULL,
                                               category    VARCHAR(255),
                                               name        VARCHAR(255)   NOT NULL,
                                               price       NUMERIC(12, 2),
                                               created_at  TIMESTAMP DEFAULT NOW(),
                                               updated_at  TIMESTAMP DEFAULT NOW(),
                                               UNIQUE (supplier_id, code)
);

-- ========================
-- VENUES
-- ========================
CREATE TABLE IF NOT EXISTS venues (
                                      id          BIGSERIAL PRIMARY KEY,
                                      name        VARCHAR(255) NOT NULL,
                                      address     VARCHAR(255),
                                      city        VARCHAR(100),
                                      postal_code VARCHAR(20),
                                      company_id  BIGINT REFERENCES companies(id),
                                      user_id     BIGINT REFERENCES users(id)
);

-- ========================
-- ORDERS
-- ========================
CREATE TABLE IF NOT EXISTS orders (
                                      id          BIGSERIAL PRIMARY KEY,
                                      company_id  BIGINT    REFERENCES companies(id),
                                      supplier_id BIGINT    REFERENCES suppliers(id),
                                      user_id     BIGINT    REFERENCES users(id),
                                      venue_id    BIGINT    REFERENCES venues(id),
                                      created_at  TIMESTAMP DEFAULT NOW(),
                                      status      VARCHAR(50)
);

-- ========================
-- ORDER_ITEMS
-- ========================
CREATE TABLE IF NOT EXISTS order_items (
                                           id                BIGSERIAL PRIMARY KEY,
                                           order_id          BIGINT         NOT NULL REFERENCES orders(id),
                                           catalogue_item_id BIGINT         NOT NULL REFERENCES catalogue_items(id),
                                           quantity          NUMERIC(12, 3) NOT NULL
);

-- ========================
-- SESSION_TOKENS
-- ========================
CREATE TABLE IF NOT EXISTS session_tokens (
                                              id            BIGSERIAL PRIMARY KEY,
                                              company_id    BIGINT UNIQUE REFERENCES companies(id),
                                              access_token  TEXT,
                                              refresh_token TEXT,
                                              expiration    TIMESTAMP
);

-- ========================
-- QUICK_LISTS
-- ========================
CREATE TABLE IF NOT EXISTS quick_lists (
                                           id          BIGSERIAL PRIMARY KEY,
                                           name        VARCHAR(255) NOT NULL,
                                           company_id  BIGINT REFERENCES companies(id),
                                           user_id     BIGINT REFERENCES users(id),
                                           supplier_id BIGINT REFERENCES suppliers(id)
);

-- ========================
-- QUICK_LIST_ITEMS
-- ========================
CREATE TABLE IF NOT EXISTS quick_list_items (
                                                id                BIGSERIAL PRIMARY KEY,
                                                quick_list_id     BIGINT         NOT NULL REFERENCES quick_lists(id),
                                                catalogue_item_id BIGINT         NOT NULL REFERENCES catalogue_items(id),
                                                quantity          NUMERIC(12, 3) NOT NULL
);

-- ========================
-- PRODUCTS
-- ========================
CREATE TABLE IF NOT EXISTS products (
                                        id             BIGSERIAL PRIMARY KEY,
                                        canonical_name VARCHAR(255) NOT NULL
);

-- ========================
-- INVOICES
-- ========================
CREATE TABLE IF NOT EXISTS invoices (
                                        id                   BIGSERIAL PRIMARY KEY,
                                        supplier_id          BIGINT REFERENCES suppliers(id),
                                        invoice_number       VARCHAR(100),
                                        invoice_datetime     TIMESTAMP,
                                        external_document_id BIGINT,
                                        company_id           BIGINT REFERENCES companies(id)
);

-- ========================
-- INVOICE_ITEMS
-- ========================
CREATE TABLE IF NOT EXISTS invoice_items (
                                             id           BIGSERIAL PRIMARY KEY,
                                             invoice_id   BIGINT         REFERENCES invoices(id),
                                             product_name VARCHAR(255),
                                             unit_price   NUMERIC(12, 2),
                                             discount     NUMERIC(5, 2),
                                             amount       NUMERIC(12, 2),
                                             product_id   BIGINT         REFERENCES products(id)
);
