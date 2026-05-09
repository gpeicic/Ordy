-- ========================
-- ROLES
-- ========================
CREATE TABLE IF NOT EXISTS roles (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT IGNORE INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_USER'),('ROLE_PROVIDER');

-- ========================
-- USERS
-- ========================
CREATE TABLE IF NOT EXISTS users (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id  BIGINT NOT NULL,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- ========================
-- COMPANIES
-- ========================
CREATE TABLE IF NOT EXISTS companies (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255),
    mer_email    VARCHAR(255),
    mer_password VARCHAR(255),
    oib          VARCHAR(20) UNIQUE,
    address      VARCHAR(255),
    city         VARCHAR(100),
    postal_code  VARCHAR(20)
);

-- ========================
-- USER_COMPANIES (junction)
-- ========================
CREATE TABLE IF NOT EXISTS user_companies (
    user_id    BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, company_id),
    CONSTRAINT fk_uc_user    FOREIGN KEY (user_id)    REFERENCES users(id),
    CONSTRAINT fk_uc_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

-- ========================
-- SUPPLIERS
-- ========================
CREATE TABLE IF NOT EXISTS suppliers (
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    oib   VARCHAR(20) UNIQUE,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255)
);

-- ========================
-- COMPANY_SUPPLIERS (junction)
-- ========================
CREATE TABLE IF NOT EXISTS company_suppliers (
    company_id  BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    PRIMARY KEY (company_id, supplier_id),
    CONSTRAINT fk_cs_company  FOREIGN KEY (company_id)  REFERENCES companies(id),
    CONSTRAINT fk_cs_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);

-- ========================
-- BLOCKED_SUPPLIERS
-- ========================
CREATE TABLE IF NOT EXISTS blocked_suppliers (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    oib  VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL
);

-- ========================
-- CATALOGUE_ITEMS
-- ========================
CREATE TABLE IF NOT EXISTS catalogue_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id BIGINT         NOT NULL,
    code        VARCHAR(100)   NOT NULL,
    category    VARCHAR(255),
    name        VARCHAR(255)   NOT NULL,
    price       DECIMAL(12, 2),
    created_at  DATETIME DEFAULT NOW(),
    updated_at  DATETIME DEFAULT NOW(),
    UNIQUE KEY uq_supplier_code (supplier_id, code),
    CONSTRAINT fk_ci_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);

-- ========================
-- VENUES
-- ========================
CREATE TABLE IF NOT EXISTS venues (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    address     VARCHAR(255),
    city        VARCHAR(100),
    postal_code VARCHAR(20),
    company_id  BIGINT,
    user_id     BIGINT,
    CONSTRAINT fk_venues_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_venues_user    FOREIGN KEY (user_id)    REFERENCES users(id)
);

-- ========================
-- ORDERS
-- ========================
CREATE TABLE IF NOT EXISTS orders (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id  BIGINT,
    supplier_id BIGINT,
    user_id     BIGINT,
    venue_id    BIGINT,
    created_at  DATETIME DEFAULT NOW(),
    status      VARCHAR(50),
    CONSTRAINT fk_orders_company  FOREIGN KEY (company_id)  REFERENCES companies(id),
    CONSTRAINT fk_orders_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    CONSTRAINT fk_orders_user     FOREIGN KEY (user_id)     REFERENCES users(id),
    CONSTRAINT fk_orders_venue    FOREIGN KEY (venue_id)    REFERENCES venues(id)
);

-- ========================
-- ORDER_ITEMS
-- ========================
CREATE TABLE IF NOT EXISTS order_items (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id          BIGINT         NOT NULL,
    catalogue_item_id BIGINT         NOT NULL,
    quantity          DECIMAL(12, 3) NOT NULL,
    CONSTRAINT fk_oi_order     FOREIGN KEY (order_id)          REFERENCES orders(id),
    CONSTRAINT fk_oi_catalogue FOREIGN KEY (catalogue_item_id) REFERENCES catalogue_items(id)
);

-- ========================
-- SESSION_TOKENS
-- ========================
CREATE TABLE IF NOT EXISTS session_tokens (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id    BIGINT UNIQUE,
    access_token  TEXT,
    refresh_token TEXT,
    expiration    DATETIME,
    CONSTRAINT fk_st_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

-- ========================
-- QUICK_LISTS
-- ========================
CREATE TABLE IF NOT EXISTS quick_lists (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    company_id  BIGINT,
    user_id     BIGINT,
    supplier_id BIGINT,
    CONSTRAINT fk_ql_company  FOREIGN KEY (company_id)  REFERENCES companies(id),
    CONSTRAINT fk_ql_user     FOREIGN KEY (user_id)     REFERENCES users(id),
    CONSTRAINT fk_ql_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);

-- ========================
-- QUICK_LIST_ITEMS
-- ========================
CREATE TABLE IF NOT EXISTS quick_list_items (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    quick_list_id     BIGINT         NOT NULL,
    catalogue_item_id BIGINT         NOT NULL,
    quantity          DECIMAL(12, 3) NOT NULL,
    CONSTRAINT fk_qli_list      FOREIGN KEY (quick_list_id)     REFERENCES quick_lists(id),
    CONSTRAINT fk_qli_catalogue FOREIGN KEY (catalogue_item_id) REFERENCES catalogue_items(id)
);

-- ========================
-- PRODUCTS
-- ========================
CREATE TABLE IF NOT EXISTS products (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    canonical_name VARCHAR(255) NOT NULL
);

-- ========================
-- INVOICES
-- ========================
CREATE TABLE IF NOT EXISTS invoices (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id          BIGINT,
    invoice_number       VARCHAR(100),
    invoice_datetime     DATETIME,
    external_document_id BIGINT,
    company_id           BIGINT,
    CONSTRAINT fk_inv_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    CONSTRAINT fk_inv_company  FOREIGN KEY (company_id)  REFERENCES companies(id)
);

-- ========================
-- INVOICE_ITEMS
-- ========================
CREATE TABLE IF NOT EXISTS invoice_items (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_id   BIGINT,
    product_name VARCHAR(255),
    unit_price   DECIMAL(12, 2),
    discount     DECIMAL(5, 2),
    amount       DECIMAL(12, 2),
    product_id   BIGINT,
    CONSTRAINT fk_ii_invoice FOREIGN KEY (invoice_id) REFERENCES invoices(id),
    CONSTRAINT fk_ii_product FOREIGN KEY (product_id) REFERENCES products(id)
);
