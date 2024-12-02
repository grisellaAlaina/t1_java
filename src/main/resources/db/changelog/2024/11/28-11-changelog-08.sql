-- liquibase formatted sql

ALTER TABLE account
    ADD COLUMN status VARCHAR(255),
    ADD COLUMN account_id UUID,
    ADD COLUMN frozen_amount NUMERIC(15, 2);