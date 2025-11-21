--liquibase formatted sql
-- changeset linh-nguyen:0001-create-employee-table
CREATE SEQUENCE employee_seq START 1 INCREMENT 1;

CREATE TABLE employee (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    birth_date DATE,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);
