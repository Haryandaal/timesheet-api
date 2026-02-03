CREATE TABLE tb_m_role
(
    id         VARCHAR(50) PRIMARY KEY,
    name       VARCHAR(100),
    level      INT,
    created_at DATETIME,
    updated_at DATETIME,
    deleted_at DATETIME
)