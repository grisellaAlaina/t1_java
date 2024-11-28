-- liquibase formatted sql

ALTER TABLE transaction
DROP CONSTRAINT fk_account,
    ALTER COLUMN account_id TYPE UUID,
    ADD status VARCHAR(25),
    ADD transaction_id UUID DEFAULT uuid_generate_v4() UNIQUE,
    ADD timestamp TIMESTAMP DEFAULT NOW(),
    ADD transaction_time TIMESTAMP DEFAULT NOW();
ALTER TABLE transaction ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account(account_id);