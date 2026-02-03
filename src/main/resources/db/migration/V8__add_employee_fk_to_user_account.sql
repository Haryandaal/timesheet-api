ALTER TABLE tb_m_user_account
    ADD COLUMN employee_id VARCHAR(50) NOT NULL;

ALTER TABLE tb_m_user_account
    ADD CONSTRAINT fk_user_account_employee
        FOREIGN KEY (employee_id)
            REFERENCES tb_m_employee (id);