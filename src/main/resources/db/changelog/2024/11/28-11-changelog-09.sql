-- liquibase formatted sql

DROP TYPE IF EXISTS transaction_status_enum;

CREATE TYPE transaction_status_enum AS ENUM ('ACCEPTED', 'REJECTED', 'BLOCKED', 'CANCELLED', 'REQUESTED');

ALTER TABLE transaction
    ADD transaction_id UUID,
    ADD COLUMN status transaction_status_enum,
    ADD COLUMN timestamp TIMESTAMP;