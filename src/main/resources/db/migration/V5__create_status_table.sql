CREATE TABLE tb_m_status
(
    id         VARCHAR(50) PRIMARY KEY,
    name       VARCHAR(100),
    created_at DATETIME,
    updated_at DATETIME,
    deleted_at DATETIME
)