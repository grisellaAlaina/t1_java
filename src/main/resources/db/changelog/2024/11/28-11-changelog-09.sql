-- liquibase formatted sql


ALTER TABLE transaction
    ADD transaction_id UUID,
    ADD COLUMN status VARCHAR(255),
    ADD COLUMN timestamp TIMESTAMP;