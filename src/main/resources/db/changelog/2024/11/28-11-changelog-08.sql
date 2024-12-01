-- liquibase formatted sql

DROP TYPE IF EXISTS account_status_enum;

CREATE TYPE account_status_enum AS ENUM ('ARRESTED', 'BLOCKED', 'CLOSED', 'OPEN');

ALTER TABLE account
    ADD COLUMN status account_status_enum,
    ADD COLUMN account_id UUID,
    ADD COLUMN frozen_amount NUMERIC(15, 2);