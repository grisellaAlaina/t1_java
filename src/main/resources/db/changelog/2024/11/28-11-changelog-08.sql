-- liquibase formatted sql

ALTER TABLE account
DROP CONSTRAINT fk_client,
    ADD frozen_amount NUMERIC(15, 2) DEFAULT 0,
    ADD account_id UUID DEFAULT uuid_generate_v4() UNIQUE,
    ADD status VARCHAR(20),
    ALTER COLUMN client_id TYPE UUID;
ALTER TABLE account ADD CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES client(client_id);