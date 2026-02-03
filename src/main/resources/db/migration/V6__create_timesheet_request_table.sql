CREATE TABLE tb_tr_timesheet_request
(
    id           VARCHAR(50) PRIMARY KEY,
    employee_id  VARCHAR(50) NOT NULL,
    status_id    VARCHAR(50),
    request_date DATETIME    NOT NULL,
    approved_by  VARCHAR(50),
    approved_at  DATETIME,
    created_at   DATETIME,
    updated_at   DATETIME,
    deleted_at   DATETIME,
    FOREIGN KEY (employee_id) REFERENCES tb_m_employee (id),
    FOREIGN KEY (status_id) REFERENCES tb_m_status (id),
    FOREIGN KEY (approved_by) REFERENCES tb_m_employee (id)
)