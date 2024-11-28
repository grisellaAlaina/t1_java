-- liquibase formatted sql

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

ALTER TABLE client
    ADD client_id UUID DEFAULT uuid_generate_v4();