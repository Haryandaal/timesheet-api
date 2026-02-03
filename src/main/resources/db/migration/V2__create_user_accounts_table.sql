CREATE TABLE tb_m_user_account
(
    id         VARCHAR(50) PRIMARY KEY,
    role_id    VARCHAR(50),
    username   VARCHAR(100) UNIQUE,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(100) UNIQUE NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    deleted_at DATETIME,
    FOREIGN KEY (role_id) REFERENCES tb_m_role (id)
)