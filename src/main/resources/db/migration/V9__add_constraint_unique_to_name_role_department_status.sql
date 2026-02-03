ALTER TABLE tb_m_role
    ADD CONSTRAINT name unique (name);

ALTER TABLE tb_m_department
    ADD CONSTRAINT name unique (name);

ALTER TABLE tb_m_status
    ADD CONSTRAINT name unique (name);