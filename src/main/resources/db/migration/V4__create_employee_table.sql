CREATE TABLE tb_m_employee
(
    id            VARCHAR(50) PRIMARY KEY,
    code          VARCHAR(50) UNIQUE,
    full_name     VARCHAR(100)        NOT NULL,
    phone_number  VARCHAR(20) UNICODE NOT NULL,
    position      VARCHAR(100),
    department_id VARCHAR(50)         NOT NULL,
    manager_id    VARCHAR(50),
    created_at    DATETIME,
    updated_at    DATETIME,
    deleted_at    DATETIME,
    FOREIGN KEY (department_id) REFERENCES tb_m_department (id),
    FOREIGN KEY (manager_id) REFERENCES tb_m_employee (id)
)