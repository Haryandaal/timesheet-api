CREATE TABLE tb_tr_timesheet_detail
(
    id                   VARCHAR(50) PRIMARY KEY,
    employee_id          VARCHAR(50) NOT NULL,
    timesheet_request_id VARCHAR(50) NOT NULL,
    work_date            DATETIME    NOT NULL,
    location_type        VARCHAR(100),
    hours_spent          DECIMAL(4, 2),
    task_description     TEXT        NOT NULL,
    created_at           DATETIME,
    updated_at           DATETIME,
    deleted_at           DATETIME,
    FOREIGN KEY (employee_id) REFERENCES tb_m_employee (id),
    FOREIGN KEY (timesheet_request_id) REFERENCES tb_tr_timesheet_request (id)
)